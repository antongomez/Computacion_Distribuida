package servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import cliente.ClienteInterfaz;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anton Gomez e Cristina Lopez
 */
public class ServidorImpl extends UnicastRemoteObject implements ServidorInterfaz {

    private final ArrayList<ClienteInterfaz> clientes;
    private final FachadaBaseDatos fBD;

    public ServidorImpl(FachadaBaseDatos fBD) throws RemoteException {
        super();
        this.fBD = fBD;
        clientes = new ArrayList<>();
    }

    @Override
    public boolean comprobarCredenciais(String usuario, String contrasenha) throws RemoteException {
        return fBD.comprobarCredenciais(usuario, contrasenha);
    }

    @Override
    public boolean rexistrarse(String usuario, String contrasinal) throws RemoteException {
        return fBD.engadirUsuario(usuario, contrasinal);
    }

    @Override
    public void conectarse(String usuario, String contrasenha, ClienteInterfaz cliente) throws RemoteException {

        synchronized (clientes) {
            ArrayList<ClienteInterfaz> amigosEnLinha = buscarAmigosEnLinha(usuario);
            if (!(amigosEnLinha.contains(cliente))) {
                cliente.recibirAmigosEnLinha(amigosEnLinha);

                for (ClienteInterfaz c : amigosEnLinha) {
                    c.notificarConexion(cliente);
                }
            }
            clientes.add(cliente);
        }

        //Enviamos a lista de solicitudes de amizade
        List<String> solicitudes = fBD.buscarSolicitantesAmizade(usuario);
        for (String s : solicitudes) {
            cliente.recibirSolicitude(s);
        }
    }

    @Override
    public void desconectarse(ClienteInterfaz cliente) throws RemoteException {

        String id = cliente.getIdUsuario();
        synchronized (clientes) {
            ArrayList<ClienteInterfaz> amigosEnLinha = buscarAmigosEnLinha(id);

            if (clientes.contains(cliente)) {
                clientes.remove(cliente);

                for (ClienteInterfaz c : amigosEnLinha) {
                    c.notificarDesconexion(cliente);
                }
            }
        }
    }

    private ArrayList<ClienteInterfaz> buscarAmigosEnLinha(String usuario) {

        List<String> amigos = fBD.buscarAmigos(usuario);
        ArrayList<ClienteInterfaz> amigosEnLinha = new ArrayList<>();

        synchronized (clientes) {
            try {
                for (ClienteInterfaz c : clientes) {
                    String idCliente;
                    idCliente = c.getIdUsuario();
                    for (String idAmigo : amigos) {
                        if (idAmigo.equals(idCliente)) {
                            amigosEnLinha.add(c);
                        }

                    }
                }

            } catch (RemoteException e) {
                System.out.println(e.getMessage());
            }
        }
        return amigosEnLinha;

    }

    //Busca os usuarios que cumpran o patrón e que non sexan amigos nin se lles enviara xa unha solicitude
    @Override
    public List<String> buscarUsuarioSolicitarAmizade(String patron, String solicitante) throws RemoteException {
        return fBD.buscarUsuariosSolicitarAmizade(patron, solicitante);
    }

    @Override
    public void solicitarAmizade(String solicitante, String receptor) throws RemoteException {
        fBD.engadirSolicitude(solicitante, receptor);
        synchronized (clientes) {
            for (ClienteInterfaz c : clientes) {
                if (c.getIdUsuario().equals(receptor)) {
                    c.recibirSolicitude(solicitante);
                }
            }
        }
    }

    @Override
    public void rexeitarSolicitude(String solicitante, String receptor
    ) {
        if (!fBD.eliminarSolicitude(solicitante, receptor)) {
            System.out.println("Non se puido eliminar a solicitude");
        }
    }

    @Override
    public void aceptarSolicitude(String solicitante, String receptor
    ) {
        if (!fBD.aceptarSolicitude(solicitante, receptor)) {
            System.out.println("Non se puido aceptar a solicitude");
        } else {
            synchronized (clientes) {
                // Se o solicitante esta en linha enviamoslle a interfaz remota do 
                // receptor da solicitude e ao receptor a do solicitante
                int indiceSolicitante = usuarioEnLinha(solicitante);
                int indiceReceptor = usuarioEnLinha(receptor);
                if ((indiceSolicitante >= 0) && (indiceReceptor >= 0)) {
                    ArrayList<ClienteInterfaz> cliente = new ArrayList<>();

                    // Enviamoslle a interfaz ao solicitante
                    cliente.add(clientes.get(indiceReceptor));
                    try {
                        clientes.get(indiceSolicitante).recibirAmigosEnLinha(cliente);
                    } catch (RemoteException ex) {
                        Logger.getLogger(ServidorImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    // Enviamoslle a interfaz ao receptor
                    cliente.clear();
                    cliente.add(clientes.get(indiceSolicitante));
                    try {
                        clientes.get(indiceReceptor).recibirAmigosEnLinha(cliente);
                    } catch (RemoteException ex) {
                        Logger.getLogger(ServidorImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
    // Devolve o indice en clientes se atopa ao usuario e senon devolve -1

    private int usuarioEnLinha(String usuario) {
        synchronized (clientes) {
            for (int i = 0; i < clientes.size(); i++) {
                try {
                    if (clientes.get(i).getIdUsuario().equals(usuario)) {
                        return i;
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(ServidorImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return -1;
    }

}
