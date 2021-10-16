package montecarlo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

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

        for (int i = 0; i < n; i++) {
            if (Par.xerarParAleatorio().estaCircunferenciaUnidade()) {
                conta++;
            }
        }

        return conta;
    }

}
