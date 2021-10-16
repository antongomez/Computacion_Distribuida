package cliente;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import montecarlo.MonteCarloInterface;
import montecarlo.Par;

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
        ArrayList<Par> pares = new ArrayList<>();
        
        for(int i = 0; i < nCalculos; i++){
            pares.add(Par.xerarParAleatorio());
        }
        
        try {
            proporcion = h.calcularProporcion(pares); 
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
