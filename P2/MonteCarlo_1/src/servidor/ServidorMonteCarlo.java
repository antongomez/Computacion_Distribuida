package servidor;

import cliente.AproxPI;
import montecarlo.MonteCarloImpl;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anton Gomez Lopez
 */
public class ServidorMonteCarlo {

    public static final String HOSTNAME = "localhost";

    public static void main(String args[]) {
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);
        String numPorto, registryURL, hostname;

        try {
            System.out.println("Introduce o nome de host: ");
            hostname = (br.readLine()).trim();
            System.out.println("Introduce o numero de porto:");
            numPorto = (br.readLine()).trim();
            int RMIPortNum = Integer.parseInt(numPorto);

            startRegistry(RMIPortNum);

            MonteCarloImpl obxectoExportado = new MonteCarloImpl();
            registryURL = "rmi://" + hostname + ":" + numPorto + "/montecarlo";
            Naming.rebind(registryURL, obxectoExportado);

            System.out.println("Servidor rexistrado. ");
            listRegistry(registryURL);
            System.out.println("Servidor listo.");

        } catch (Exception e) {
            Logger.getLogger(AproxPI.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /* Este metodo inicia un rexistro RMI no localhost se non 
        existe xa no porto indicado. */
    private static void startRegistry(int RMIPortNum) throws RemoteException {

        try {
            Registry registry = LocateRegistry.getRegistry(RMIPortNum);
            registry.list();

        } catch (RemoteException e) {

            System.out.println("O rexistro RMI non puido ser localizado no porto "
                    + RMIPortNum);
            Registry registry
                    = LocateRegistry.createRegistry(RMIPortNum);
            System.out.println(
                    "Rexistro RMI creado no porto " + RMIPortNum);
        }
    }

    // This method lists the names registered with a Registry object
    private static void listRegistry(String registryURL) throws RemoteException, MalformedURLException {

        System.out.println("O rexistro " + registryURL + " conten: ");
        String[] names = Naming.list(registryURL);
        for (int i = 0; i < names.length; i++) {
            System.out.println(names[i]);
        }
    }

}
