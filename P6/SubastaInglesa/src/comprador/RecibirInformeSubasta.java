package comprador;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import main.Subasta;

/**
 *
 * @author User
 */
public class RecibirInformeSubasta extends CyclicBehaviour{
    private InterfazComprador interfaz;

    public RecibirInformeSubasta(InterfazComprador interfaz) {
        this.interfaz = interfaz;
    }

    @Override
    public void action() {
        MessageTemplate mtInformar = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        ACLMessage msxInformar = myAgent.receive(mtInformar);
        
        if (msxInformar != null) {
            String[] campos = msxInformar.getContent().split(":");
            Subasta subasta = interfaz.subastas.get(campos[0]);
            if (subasta != null) {
                String ganhador = campos[1];
                System.out.println(myAgent.getLocalName() + ": inform recibido con: " + campos[0] + ", " + campos[1]);

                if (ganhador.equals(myAgent.getLocalName())) {
                    ((Comprador) myAgent).eliminarLibroCatalogo(subasta.getTitulo());
                    System.out.println(myAgent.getLocalName() + ": son o comprador ganhador de " + campos[0]);
                }
                subasta.setGanhador(ganhador);
                subasta.setEstado(Subasta.ESTADO_2);
                interfaz.actualizarSubasta(subasta);
            }

        } else {
            block();
        }
    }
    
}
