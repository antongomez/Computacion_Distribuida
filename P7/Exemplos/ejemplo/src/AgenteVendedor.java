package com.distribuida.onto.ejemploOntologia;

import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import com.distribuida.onto.ejemploOntologia.ontologia.Oferta;
import com.distribuida.onto.ejemploOntologia.ontologia.Ofertar;
import com.distribuida.onto.ejemploOntologia.ontologia.OntologiaVinilos;
import com.distribuida.onto.ejemploOntologia.ontologia.Pedir;
import com.distribuida.onto.ejemploOntologia.ontologia.Vinilo;

public class AgenteVendedor extends Agent {

	private Ontology onto;
	private Codec codec;
	private Vinilo vinilo;
	private Float precio;

	private class VentaBehaviour extends CyclicBehaviour {

		private MessageTemplate mt;

		@Override
		public void onStart() {
			System.out.println("Hola! Soy Vendedor: " + getLocalName());
			mt = MessageTemplate.and(MessageTemplate.MatchOntology(onto.getName()), MessageTemplate.MatchLanguage(codec.getName()));
		}

		@Override
		public void action() {

			ACLMessage msg = myAgent.receive(mt);
			if (msg == null) {
				block();
			} else {
				try {
					Action a = (Action) getContentManager().extractContent(msg);
					Pedir p = (Pedir) a.getAction();

					if (p.getVinilo().getArtista().equals(vinilo.getArtista()) && p.getVinilo().getTitulo().equals(vinilo.getTitulo())) {
						ACLMessage rp = msg.createReply();
						Ofertar of = new Ofertar();
						Oferta o = new Oferta();
						o.setPrecio(precio);
						o.setProducto(vinilo);
						of.setOferta(o);

						getContentManager().fillContent(rp, new Action(myAgent.getAID(), of));

						rp.setPerformative(ACLMessage.PROPOSE);
						myAgent.send(rp);

					}
				} catch (CodecException | OntologyException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	protected void setup() {
		super.setup();

		// registro en pag. amarillas
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("tienda");
		sd.setName(getName());
		sd.setOwnership("ComDis");
		dfd.setName(getAID());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		vinilo = new Vinilo();
		vinilo.setArtista("ComDis");
		vinilo.setTitulo("Ontologia");
		precio = (float) (10 * Math.random());

		codec = new SLCodec();
		onto = OntologiaVinilos.getInstance();
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(onto);

		this.addBehaviour(new VentaBehaviour());
	}

	@Override
	protected void takeDown() {
		super.takeDown();
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}
}
