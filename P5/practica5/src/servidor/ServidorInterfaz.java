package servidor;

import java.rmi.Remote;
import cliente.ClienteInterfaz;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author Anton Gomez e Cristina Lopez
 */
public interface ServidorInterfaz extends Remote {

    public boolean comprobarCredenciais(String usuario, String contrasenha) throws RemoteException;

    public boolean rexistrarse(String usuario, String contrasinal) throws RemoteException;

    public void conectarse(String usuario, String contrasenha, ClienteInterfaz cliente) throws RemoteException;

    public void desconectarse(String contrasenha, ClienteInterfaz cliente) throws RemoteException;
    
    public List<String> buscarUsuarioSolicitarAmizade(String patron, String solicitante, String contrasenha) throws RemoteException;
    
    public void solicitarAmizade(String solicitante, String receptor, String contrasenha) throws RemoteException;
    
    public void rexeitarSolicitude(String solicitante, String receptor, String contrasenha) throws RemoteException;
    
    public void aceptarSolicitude(String solicitante, String receptor, String contrasenha) throws RemoteException;

}
