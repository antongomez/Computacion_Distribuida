package vendedor;

import main.Subasta;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import java.util.HashMap;

/**
 *
 * @author Anton Gomez Lopez
 */
public class Vendedor extends Agent {

    // O titulo do libro a vender
    private HashMap<String, Subasta> subastas;
    private HashMap<String, Integer> idSubastas;
    private InterfazVendedor gui;

    // Inicializacion do axente
    @Override
    protected void setup() {
        // Creamos o catalogo
        subastas = new HashMap<>();
        idSubastas = new HashMap<>();

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
        Subasta subasta = new Subasta(idSubasta, titulo, incremento, prezo, 0, " - ");
        subastas.put(idSubasta, subasta);

        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                System.out.println(titulo + " introducido no catalogo. Prezo = " + prezo);

                // Engadimos un comportamento que inicia a subasta
                myAgent.addBehaviour(new SubastarLibro(titulo, prezo, incremento, subastas.get(idSubasta)));
            }
        });

        return subasta;
    }

    public InterfazVendedor getGui() {
        return gui;
    }

}
