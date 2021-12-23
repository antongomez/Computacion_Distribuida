package comprador;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import main.Subasta;

/**
 *
 * @author Anton Gomez Lopez
 */
public class BuscarSubasta extends CyclicBehaviour {

    private final InterfazComprador interfaz;

    public BuscarSubasta(InterfazComprador interfaz) {
        super();
        this.interfaz = interfaz;
    }

    @Override
    public void action() {
        MessageTemplate mtOferta = MessageTemplate.MatchPerformative(ACLMessage.CFP);
        ACLMessage msxOferta = myAgent.receive(mtOferta);
        MessageTemplate mtAceptar = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
        ACLMessage msxAceptar = myAgent.receive(mtAceptar);
        MessageTemplate mtRexeitar = MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL);
        ACLMessage msxRexeitar = myAgent.receive(mtRexeitar);

        if (msxOferta != null) {

            // CFP recibido, comprobamos se nos interesa
            String[] datos = msxOferta.getContent().split(":");
            String tituloRecibido = datos[0];
            Float prezoSubasta = Float.parseFloat(datos[1]);
            String idSubasta = datos[2];
            ACLMessage reply = msxOferta.createReply();

            Float prezoMax = ((Comprador) myAgent).libros.get(tituloRecibido);

            // En caso de que non interese non respondemos ou de que o prezo supere o que podemos pagar
            if ((prezoMax != null) && (prezoSubasta <= prezoMax)) {
                // O libro e de interese, respondemos para entrar na subasta
                reply.setPerformative(ACLMessage.PROPOSE);
                myAgent.send(reply);

                Subasta subasta = interfaz.subastas.get(idSubasta);
                if (subasta == null) {
                    subasta = new Subasta(idSubasta, tituloRecibido, prezoSubasta, 0, " - ");
                    subasta.setEstado(Subasta.ESTADO_1);
                    System.out.println(myAgent.getLocalName() + ": axente engandindo " + idSubasta);
                    interfaz.subastas.put(idSubasta, subasta);
                } else {
                    subasta.setPrezoActual(prezoSubasta);
                    subasta.setRonda(subasta.getRonda() + 1);
                }

                interfaz.actualizarSubasta(subasta);

            } else if (prezoMax == null) {
                System.out.println(myAgent.getLocalName() + ": o libro non me interesa " + idSubasta);
            }
        } else if (msxAceptar != null) {
            Subasta subasta = interfaz.subastas.get(msxAceptar.getContent());
            // Se recibimos un acept xa sabemos que somos os ganhadores da ronda
            subasta.setGanhador(myAgent.getLocalName());
            interfaz.actualizarSubasta(subasta);

        } else if (msxRexeitar != null) {
            String[] campos = msxRexeitar.getContent().split(":");
            Subasta subasta = interfaz.subastas.get(campos[0]);
            String ganhador = campos[1];

            // Se recibimos un REJECT significa que o axente non ganhou a puxa nesta ronda
            subasta.setGanhador(ganhador);
            interfaz.actualizarSubasta(subasta);

        } else {
            block();
        }

    }
}
