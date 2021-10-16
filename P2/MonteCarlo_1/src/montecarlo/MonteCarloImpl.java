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
    public Integer calcularProporcion(List<Par> pares) throws RemoteException {
        int conta = 0;

        for (Par p : pares) {
            if (p.estaCircunferenciaUnidade()) {
                conta++;
            }
        }

        return conta;
    }

}
