package vendedor;

import main.Subasta;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;

/**
 *
 * @author Anton Gomez Lopez
 */
public class SubastarLibro extends Behaviour {

    private final String titulo;
    private float prezo;
    private final float incremento;
    private int step = 0;
    private ArrayList<AID> axentesCompradores;
    private final ArrayList<AID> axentesRonda;
    private final ArrayList<AID> axentesParticipantes;
    private AID axenteGanhador;
    private int ronda;
    private MessageTemplate mt;
    private final Subasta subasta;

    public SubastarLibro(String titulo, float prezoInicial, int incremento, Subasta subasta) {
        super();
        this.titulo = titulo;
        this.prezo = prezoInicial;
        this.incremento = incremento;
        this.ronda = 0;
        this.subasta = subasta;
        this.axentesRonda = new ArrayList<>();
        this.axentesParticipantes = new ArrayList<>();
        this.axentesCompradores = new ArrayList<>();

    }

    @Override
    public void action() {
        switch (step) {
            case 0:
                ((Vendedor) myAgent).getGui().actualizarSubasta(subasta);

                buscarCompradores();

                if (axentesCompradores.size() > 0) {
                    String replyWith = enviarCfp();
                    // Preparamos a plantilla para a resposta dos compradores
                    mt = MessageTemplate.and(MessageTemplate.MatchConversationId(titulo),
                            MessageTemplate.MatchInReplyTo(replyWith));

                    // Posto que hai posibles compradores, mandamoslle un cfp para ver se estan interesados
                    step = 1;
                    myAgent.addBehaviour(new CambiarRonda(myAgent, 10000, this));

                } else {
                    block(5000);
                }
                break;
            case 1:
                // Recibimos todas as propose
                ACLMessage reply = myAgent.receive(mt);
                if (reply != null) {
                    // Contestacion recibida
                    if (step != 1) {
                        System.out.println(myAgent.getLocalName() + ": o axente respondeu fora de tempo");
                    } else {
                        contestarPropose(reply);
                    }
                } else {
                    block(2000);
                }
                break;

            case 2:
                if (axentesRonda.isEmpty()) {
                    ronda++;
                    subasta.setRonda(ronda);
                    prezo += incremento;
                    subasta.setPrezoActual(prezo);
                    ((Vendedor) myAgent).getGui().actualizarSubasta(subasta);
                    step = 0;
                }
                break;
            case 3:
                ACLMessage informe = new ACLMessage(ACLMessage.INFORM);
                System.out.println(myAgent.getLocalName() + ": axentes participantes na subasta " + subasta.getIdSubasta() + ": ");
                for (AID axente : axentesParticipantes) {
                    System.out.println(axente.getLocalName());
                    informe.addReceiver(axente);
                    informe.setContent(subasta.getIdSubasta() + ":" + axenteGanhador.getLocalName());
                }
                informe.setConversationId(titulo);
                myAgent.send(informe);
                System.out.println(myAgent.getLocalName() + ": o ganhador da subasta e: " + axenteGanhador.getLocalName());
                step = 4;
                break;

        }
    }

    // Funcion que consulta os axentes compradores no servizo de paxinas amarelas
    private void buscarCompradores() {
        DFAgentDescription plant = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("comprador-libros");
        plant.addServices(sd);
        try {
            DFAgentDescription[] result = DFService.search(myAgent, plant);
            axentesCompradores.clear();
            for (DFAgentDescription r : result) {
                axentesCompradores.add(r.getName());
            }
        } catch (FIPAException fe) {
            System.out.println("\nErro obtendo os compradores\n");
        }
    }

    // Funcion que envia un call for proposal a todos os compradores atopados
    // co titulo, prezo e id da subasta
    private String enviarCfp() {
        ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
        for (int i = 0; i < axentesCompradores.size(); ++i) {
            cfp.addReceiver(axentesCompradores.get(i));
        }
        cfp.setContent(titulo + ":" + prezo + ":" + subasta.getIdSubasta());
        cfp.setConversationId(titulo);
        cfp.setReplyWith(subasta.getIdSubasta() + System.currentTimeMillis()); // Unico valor
        myAgent.send(cfp);

        return cfp.getReplyWith();
    }

    // Funcion que envia o accpet e o reject
    private void contestarPropose(ACLMessage reply) {
        axentesRonda.add(reply.getSender());

        // Enviamos unha mensaxe indicando que e o ganahdor // da ronda cun accept proposal
        ACLMessage comunicarResultado;
        // O ganhador da ronda e o primeiro do arraylist
        if (axentesRonda.size() == 1) {
            if (ronda == 0) {
                subasta.setEstado(Subasta.ESTADO_1);
            }
            comunicarResultado = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
            comunicarResultado.setContent(subasta.getIdSubasta());
            axenteGanhador = reply.getSender();
            subasta.setGanhador(axenteGanhador.getLocalName());
            ((Vendedor) myAgent).getGui().actualizarSubasta(subasta);
        } else {
            comunicarResultado = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
            comunicarResultado.setContent(subasta.getIdSubasta() + ":" + axenteGanhador.getLocalName());
        }
        comunicarResultado.addReceiver(reply.getSender());
        comunicarResultado.setConversationId(titulo);
        myAgent.send(comunicarResultado);
    }

    @Override
    public boolean done() {
        return (step == 4);
    }

    protected void engadirAxentesRonda() {
        for (AID axente : axentesRonda) {
            engadirAxenteParticipante(axente);
        }
    }

    private void engadirAxenteParticipante(AID axente) {
        if (!axentesParticipantes.contains(axente)) {
            axentesParticipantes.add(axente);
        }
    }

    public float getPrezo() {
        return prezo;
    }

    public void setPrezo(float prezo) {
        this.prezo = prezo;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public ArrayList<AID> getAxentesCompradores() {
        return axentesCompradores;
    }

    public void setAxentesCompradores(ArrayList<AID> axentesCompradores) {
        this.axentesCompradores = axentesCompradores;
    }

    public AID getAxenteGanhador() {
        return axenteGanhador;
    }

    public void setAxenteGanhador(AID axenteGanhador) {
        this.axenteGanhador = axenteGanhador;
    }

    public int getRonda() {
        return ronda;
    }

    public void setRonda(int ronda) {
        this.ronda = ronda;
    }

    public MessageTemplate getMt() {
        return mt;
    }

    public void setMt(MessageTemplate mt) {
        this.mt = mt;
    }

    public String getTitulo() {
        return titulo;
    }

    public float getIncremento() {
        return incremento;
    }

    public ArrayList<AID> getAxentesRonda() {
        return axentesRonda;
    }

    public ArrayList<AID> getAxentesParticipantes() {
        return axentesParticipantes;
    }

    public Subasta getSubasta() {
        return subasta;
    }

}
