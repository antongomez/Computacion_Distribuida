package vendedor;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import java.util.HashMap;
import ontoloxiaSubasta.Subasta;
import ontoloxiaSubasta.Subasta_inglesaOntology;
import ontoloxiaSubasta.impl.DefaultSubasta;

/**
 *
 * @author Anton Gomez Lopez
 */
public class Vendedor extends Agent {

    // O titulo do libro a vender
    private HashMap<String, Integer> idSubastas;
    private InterfazVendedor gui;

    private final Codec codec = new SLCodec();
    private final Ontology ontoloxia = Subasta_inglesaOntology.getInstance();

    // Inicializacion do axente
    @Override
    protected void setup() {
        // Creamos o catalogo
        idSubastas = new HashMap<>();

        getContentManager().setValidationMode(false);
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontoloxia);

        gui = new InterfazVendedor(this);
        gui.setTitle(getLocalName());
        gui.setVisible(true);
    }

    // Aqui ponhemos as operacions de limpeza que se deben executar ao rematar 
    // a execucion do axente
    @Override
    protected void takeDown() {
        // Imprimimos unha mensaxe de despedida
        System.out.println("Axente vendedor " + getAID().getName() + " rematando. ");
    }

    public Subasta actualizarCatalogo(String titulo, float prezo, int incremento) {
        Integer num = idSubastas.get(titulo);
        String idSubasta;
        if (num == null) {
            idSubasta = titulo + "0";
            idSubastas.put(titulo, 1);
        } else {
            idSubasta = titulo + num;
            idSubastas.replace(titulo, num, num + 1);
        }
        Subasta subasta = new DefaultSubasta();
        subasta.setIdSubasta(idSubasta);
        subasta.setTitulo(titulo);
        subasta.setIncremento(incremento);
        subasta.setPrezo(prezo);
        subasta.setRonda(0);
        subasta.setEstado(Subasta.ESTADO_0);

        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                System.out.println(titulo + " introducido no catalogo. Prezo = " + prezo);

                // Engadimos un comportamento que inicia a subasta
                myAgent.addBehaviour(new SubastarLibro(titulo, prezo, incremento, subasta, gui));
            }
        });

        return subasta;
    }

    public Codec getCodec() {
        return codec;
    }

    public Ontology getOntoloxia() {
        return ontoloxia;
    }

}
