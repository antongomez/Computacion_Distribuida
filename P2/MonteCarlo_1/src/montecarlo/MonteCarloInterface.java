package montecarlo;

import java.rmi.*;
import java.util.List;

/**
 *
 * @author Anton Gomez Lopez
 */
public interface MonteCarloInterface extends Remote {

    /**
     * Este metodo remoto calcula cantos pares dos recibidos estan dentro da 
     * circunferencia unidade
     *
     * @param pares - lista de puntos xerados no rectangulo [0,1]x[0,1]
     * @return numero de puntos que estan dentro da cricunferencia unidade.
     * @throws java.rmi.RemoteException
     */
    public Integer calcularProporcion(List<Par> pares) throws java.rmi.RemoteException;

}
