package comprador;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.util.HashMap;
import javax.swing.WindowConstants;

/**
 *
 * @author Anton Gomez Lopez
 */
public class Comprador extends Agent {

    protected HashMap<String, Float> libros;
    private InterfazComprador gui;

    // Inicializacion do axente
    @Override
    protected void setup() {
        // Rexistramos o comprador no rexistro de paxinas amarelas
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("comprador-libros");
        sd.setName("JADE-subasta-libros");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        libros = new HashMap<>();

        gui = new InterfazComprador(this);
        gui.setTitle(getLocalName());
        gui.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        gui.setVisible(true);

        addBehaviour(new BuscarSubasta(gui));
        addBehaviour(new RecibirInformeSubasta(gui));
    }

    // Put agent clean-up operations here 
    @Override
    protected void takeDown() {
        // Eliminamos do rexistro de paxinas amarelas o axente comprador
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        // Imprimimos unha mensaxe conforme o axente marcha
        System.out.println("Axente comprador " + getAID().getName() + " rematando. ");
    }

    public void actualizarLibrosInterese(String titulo, float prezoMax) {
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                libros.put(titulo, prezoMax);
            }
        });
    }

    public HashMap<String, Float> getLibros() {
        return libros;
    }

    protected void eliminarLibroCatalogo(String titulo) {
        this.libros.remove(titulo);
    }

}
