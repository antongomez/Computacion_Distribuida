package montecarlo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author Anton Gomez Lopez
 */
public class MonteCarloImpl extends UnicastRemoteObject implements MonteCarloInterface {

    public MonteCarloImpl() throws RemoteException {
        super();
    }

    @Override
    public Integer calcularProporcion(int n) throws RemoteException {
        int conta = 0;

        Date d = new Date();
        Random r = new Random(d.getTime());

        for (int i = 0; i < n; i++) {
            if (Par.xerarParAleatorio(r).estaCircunferenciaUnidade()) {
                conta++;
            }
        }

        return conta;
    }

}
