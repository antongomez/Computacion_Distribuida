package cliente;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import montecarlo.MonteCarloInterface;

/**
 *
 * @author Anton Gomez Lopez
 */
public class AproxPI extends Thread {

    
    private MonteCarloInterface h;
    private int proporcion;
    private final Integer nCalculos;

    public AproxPI(MonteCarloInterface h, Integer nCalculos) {
        this.h = h;
        this. nCalculos = nCalculos;
    }

    @Override
    public void run() {   
        try {
            proporcion = h.calcularProporcion(nCalculos); 
        } catch (RemoteException ex) {
            Logger.getLogger(AproxPI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getProporcion() {
        return proporcion;
    }

    public Integer getnCalculos() {
        return nCalculos;
    }
    
    

}
