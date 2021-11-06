package montecarlo;

import java.rmi.*;

/**
 *
 * @author Anton Gomez Lopez
 */
public interface MonteCarloInterface extends Remote {

    /**
     * Este metodo remoto xera n puntos dentro do rectangulo [0,1]x[0,1] e calcula
     * cantos estan dentro da circunferencia unidade
     *
     * @param n - Numero de puntos que se deben xerar e comprobar se estan na circunferencia
     * @return numero de puntos que estan dentro da cricunferencia unidade.
     * @throws java.rmi.RemoteException
     */
    public Integer calcularProporcion(int n) throws java.rmi.RemoteException;

}
