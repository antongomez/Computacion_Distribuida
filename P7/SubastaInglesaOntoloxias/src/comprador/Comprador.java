package comprador;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import ontoloxiaSubasta.Subasta_inglesaOntology;

/**
 *
 * @author Anton Gomez Lopez
 */
public class Comprador extends Agent {

    protected HashMap<String, Float> libros;
    private InterfazComprador gui;

    private final Codec codec = new SLCodec();
    private final Ontology ontoloxia = Subasta_inglesaOntology.getInstance();

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

        getContentManager().setValidationMode(false);
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontoloxia);

        gui = new InterfazComprador(this);
        gui.setTitle(getLocalName());

        gui.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        gui.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        Comprador comprador = this;
        gui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Se pide una confirmación antes de finalizar el programa
                int option = JOptionPane.showConfirmDialog(
                        gui,
                        "Seguro que queres finalizar a execucion do axente " + gui.getTitle() + "?",
                        "Confirmacion de finalziacion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (option == JOptionPane.YES_OPTION) {
                    gui.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    comprador.doDelete();
                }
            }
        });

        gui.setVisible(true);

        addBehaviour(new BuscarSubasta(gui, this));
    }

    // Put agent clean-up operations here 
    @Override
    protected void takeDown() {
        // Eliminamos do rexistro de paxinas amarelas o axente comprador
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            // Non facemos nada se non estaba rexistrado
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

    public Codec getCodec() {
        return codec;
    }

    public Ontology getOntoloxia() {
        return ontoloxia;
    }

}
