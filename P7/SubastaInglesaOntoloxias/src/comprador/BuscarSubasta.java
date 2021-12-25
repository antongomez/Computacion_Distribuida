package comprador;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.logging.Level;
import java.util.logging.Logger;
import ontoloxiaSubasta.EnviarRecibo;
import ontoloxiaSubasta.Informar;
import ontoloxiaSubasta.Interesa;
import ontoloxiaSubasta.Subasta;
import ontoloxiaSubasta.impl.DefaultSubasta;
import ontoloxiaSubasta.impl.DefaultInteresa;

/**
 *
 * @author Anton Gomez Lopez
 */
public class BuscarSubasta extends CyclicBehaviour {

    private final InterfazComprador interfaz;
    private final Comprador comprador;

    public BuscarSubasta(InterfazComprador interfaz, Comprador comprador) {
        super();
        this.interfaz = interfaz;
        this.comprador = comprador;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchLanguage(comprador.getCodec().getName()),
                MessageTemplate.MatchOntology(comprador.getOntoloxia().getName()));
        ACLMessage msx = myAgent.receive(mt);

        if (msx != null) {
            switch (msx.getPerformative()) {
                case ACLMessage.CFP: {
                    ContentElement ce;
                    try {
                        // CFP recibido, comprobamos se nos interesa
                        ce = comprador.getContentManager().extractContent(msx);
                        if (ce instanceof Interesa) {
                            Interesa interesa = (DefaultInteresa) ce;
                            Subasta subasta = (DefaultSubasta) interesa.getSubasta();
                            ACLMessage reply = msx.createReply();
                            Float prezoMax = ((Comprador) myAgent).libros.get(subasta.getTitulo());

                            // En caso de que non interese non respondemos ou de que o prezo supere o que podemos pagar
                            if ((prezoMax != null) && (subasta.getPrezo() <= prezoMax)) {
                                // O libro e de interese, respondemos para entrar na subasta
                                reply.setPerformative(ACLMessage.PROPOSE);
                                myAgent.send(reply);

                                int indice = participoSubasta(subasta.getIdSubasta());
                                if (indice == -1) {
                                    interfaz.subastas.add(subasta);
                                } else {
                                    actualizarSubasta(subasta, indice);
                                }

                                interfaz.actualizarSubasta(subasta);

                            }
                        }
                    } catch (Codec.CodecException | OntologyException ex) {
                        Logger.getLogger(BuscarSubasta.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case ACLMessage.ACCEPT_PROPOSAL:
                case ACLMessage.REJECT_PROPOSAL: {
                    try {
                        Action ac = (Action) comprador.getContentManager().extractContent(msx);
                        Informar informar = (Informar) ac.getAction();
                        // Se recibimos un acept xa sabemos que somos os ganhadores da ronda
                        Subasta subasta = (DefaultSubasta) informar.getSubasta();
                        actualizarSubasta(subasta, participoSubasta(subasta.getIdSubasta()));
                        interfaz.actualizarSubasta(subasta);

                    } catch (Codec.CodecException | OntologyException ex) {
                        Logger.getLogger(BuscarSubasta.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case ACLMessage.INFORM: {

                    try {
                        Action ac = (Action) comprador.getContentManager().extractContent(msx);
                        Informar informar = (Informar) ac.getAction();
                        Subasta subasta = (DefaultSubasta) informar.getSubasta();
                        int indice = participoSubasta(subasta.getIdSubasta());
                        if (indice != -1) {
                            actualizarSubasta(subasta, indice);

                            interfaz.actualizarSubasta(subasta);

                            if (subasta.getGanhador().equals(myAgent.getAID())) {
                                comprador.eliminarLibroCatalogo(subasta.getTitulo());
                                System.out.println(myAgent.getLocalName() + ": son o comprador ganhador de " + subasta.getIdSubasta());
                            }
                        }

                    } catch (Codec.CodecException | OntologyException ex) {
                        Logger.getLogger(BuscarSubasta.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case ACLMessage.REQUEST: {
                    try {
                        Action ac = (Action) comprador.getContentManager().extractContent(msx);
                        EnviarRecibo er = (EnviarRecibo) ac.getAction();
                        String idSubasta = er.getIdSubasta();
                        if (myAgent.getAID().equals(er.getComprador())) {
                            Float prezo = er.getPrezo();
                            String titulo = er.getTitulo();
                            AID vendedor = er.getVendedor();
                            System.out.println(myAgent.getLocalName() + ": iniciando "
                                    + "tramite para pagarlle a " + vendedor.getName()
                                    + " " + prezo + "€, pola subasta " + idSubasta
                                    + " de " + titulo);
                        } else {
                            System.out.println(myAgent.getLocalName() + ": non son o comprador de " + idSubasta);
                        }

                    } catch (Codec.CodecException | OntologyException ex) {
                        Logger.getLogger(BuscarSubasta.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;

                default:
                    block();
                    break;
            }
        } else {
            block();
        }

    }

    private int participoSubasta(String idSubasta) {
        for (int i = 0; i < interfaz.subastas.size(); i++) {
            if (interfaz.subastas.get(i).getIdSubasta().equals(idSubasta)) {
                return i;
            }
        }
        return -1;
    }

    private void actualizarSubasta(Subasta subasta, int indice) {
        interfaz.subastas.set(indice, subasta);
    }
}
