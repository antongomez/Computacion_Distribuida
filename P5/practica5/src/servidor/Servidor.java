package servidor;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anton Gomez e Cristina Lopez
 */
public class Servidor {

    public static void main(String args[]) {

        FachadaBaseDatos fBD = new FachadaBaseDatos();
        fBD.conexionBD();

        String registryURL;

        try {
            startRegistry(6000);
            ServidorInterfaz obxectoRemoto = new ServidorImpl(fBD);
            registryURL = "rmi://localhost:6000/callback";
            Naming.rebind(registryURL, obxectoRemoto);
            System.out.println("Servidor listo");
        } catch (RemoteException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //This method starts a RMI registry on the local host, if
    //it does not already exists at the specified port number.
    private static void startRegistry(int RMIPortNum) throws RemoteException {
        try {
            Registry rexistro = LocateRegistry.getRegistry(RMIPortNum);
            rexistro.list();
            // This call will throw an exception
            // if the registry does not already exist
        } catch (RemoteException e) {
            // No valid registry at that port.
            Registry rexistro = LocateRegistry.createRegistry(RMIPortNum);
        }
    }

}
