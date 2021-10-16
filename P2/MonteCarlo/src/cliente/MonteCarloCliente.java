package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import montecarlo.MonteCarloInterface;

/**
 *
 * @author Anton Gomez Lopez
 */
public class MonteCarloCliente {
    
    public static final Integer N = 5000000;

    public static void main(String args[]) throws InterruptedException {
        ArrayList<AproxPI> fios = new ArrayList<>();
        int[] nCalculos;

        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);
        String registryURL, hostname;
        Integer nServidores, numPorto;

        try {
            System.out.println("Numero de servidores dispoñibles: ");
            nServidores = Integer.parseInt((br.readLine()).trim());
            nCalculos = repartirCalculo(nServidores);

            for (int i = 0; i < nServidores; i++) {
                System.out.println("Introduce o nome de host do servidor: ");
                hostname = (br.readLine()).trim();
                System.out.println("Introduce o numero de porto do servidor: ");
                numPorto = Integer.parseInt((br.readLine()).trim());
                registryURL = "rmi://" + hostname + ":" + numPorto + "/montecarlo";

                // Buscamos o obxecto remoto
                MonteCarloInterface h = (MonteCarloInterface) Naming.lookup(registryURL);
                // Executamos os fios que se encargan de xerar os puntos e obter 
                // a proporcion de cantos caen dentro da circunferencia
                AproxPI fio = new AproxPI(h, nCalculos[i]);
                fios.add(fio);
                fio.start();
            }

            // Esperamos polos fios
            for (AproxPI f : fios) {
                f.join();
            }

        } catch (IOException | NotBoundException e) {
            Logger.getLogger(AproxPI.class.getName()).log(Level.SEVERE, null, e);
        }

        // Calculamos a suma das proporcions
        Double pi = (double) 0;
        for (AproxPI f : fios) {
            pi += f.getProporcion();
        }

        // Multiplicamos por catro e dividimos entre o numero total de putos xerados
        pi = 4 * pi / N;

        System.out.println("Aproximacion de PI: " + pi);

    }

    private static int[] repartirCalculo(int nServidores) {
        int[] repartoCalculos = new int[nServidores];
        int repartoInicial = Math.floorDiv(N, nServidores);
        int modulo = N % nServidores;

        for (int i = 0; i < nServidores; i++) {
            repartoCalculos[i] = repartoInicial;
            if (i < modulo) {
                repartoCalculos[i]++;
            }
        }

        if (modulo != 0) {

        }

        return repartoCalculos;
    }
}
