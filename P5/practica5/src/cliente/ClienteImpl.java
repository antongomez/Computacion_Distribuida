package cliente;

import gui.controlador.ControladorChat;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 *
 * @author Anton Gomez e Cristina Lopez
 */
public class ClienteImpl extends UnicastRemoteObject implements ClienteInterfaz {

    private final ControladorChat controlador;

    public ClienteImpl(ControladorChat controlador) throws RemoteException {
        super();
        this.controlador = controlador;
    }

    @Override
    public void recibirAmigosEnLinha(List<ClienteInterfaz> clientes) {
        for (ClienteInterfaz c : clientes) {
            controlador.engadirEnLinha(c);
        }
    }

    @Override
    public void recibirMensaxe(String mns, String idUsuario) {
        controlador.recibirMensaxe(mns, idUsuario);
    }

    @Override
    public void notificarConexion(ClienteInterfaz cliente) {
        controlador.engadirEnLinha(cliente);
    }

    @Override
    public void notificarDesconexion(ClienteInterfaz cliente) {
        controlador.eliminarEnLinha(cliente);
    }

    @Override
    public void recibirSolicitude(String usuario) throws RemoteException {
        controlador.recibirSolicitude(usuario);
    }

    @Override
    public String getIdUsuario() {
        return controlador.getNomeUser();
    }
    
    @Override
    public int getFotoPerfil() {
        return controlador.getNumFotoPerfil();
    }

}
