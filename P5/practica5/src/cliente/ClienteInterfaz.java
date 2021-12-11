package cliente;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author Anton Gomez e Cristina Lopez
 */
public interface ClienteInterfaz extends Remote {

    public void recibirAmigosEnLinha(List<ClienteInterfaz> clientes) throws RemoteException;

    public void notificarConexion(ClienteInterfaz cliente) throws RemoteException;

    public void notificarDesconexion(String clienteDesconectado) throws RemoteException;
    
    public void recibirMensaxe(String mns, String idUsuario) throws RemoteException;
    
    public void recibirSolicitude(String usuario) throws RemoteException;

    public String getIdUsuario() throws RemoteException;
    
    public int getFotoPerfil() throws RemoteException;
    

}
