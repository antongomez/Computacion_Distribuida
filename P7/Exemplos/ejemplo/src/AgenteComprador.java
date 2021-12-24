package com.distribuida.onto.ejemploOntologia;

import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.List;

import com.distribuida.onto.ejemploOntologia.ontologia.Ofertar;
import com.distribuida.onto.ejemploOntologia.ontologia.OntologiaVinilos;
import com.distribuida.onto.ejemploOntologia.ontologia.Pedir;
import com.distribuida.onto.ejemploOntologia.ontologia.Vinilo;

@SuppressWarnings("serial")
public class AgenteComprador extends Agent {

	private Codec codec;
	private Ontology onto;
	private List<AID> vendedores;

	private class CompraBehaviour extends OneShotBehaviour {

		@Override
		public void action() {
			System.out.println("Hola! Soy Comprador: " + getLocalName());
			ACLMessage msg = new ACLMessage(ACLMessage.CFP);
			msg.setOntology(onto.getName());
			msg.setLanguage(codec.getName());
			Vinilo v = new Vinilo();
			v.setArtista("ComDis");
			v.setTitulo("Ontologia");
			Pedir p = new Pedir();
			p.setVinilo(v);

			try {
				getContentManager().fillContent(msg, new Action(getAID(), p));
			} catch (CodecException | OntologyException e) {
				e.printStackTrace();
			}
			for (AID a : vendedores)
				msg.addReceiver(a);
			myAgent.send(msg);

			ACLMessage k;
			int count = 0;
			MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchOntology(onto.getName()), MessageTemplate.MatchLanguage(codec.getName()));
			while (count < vendedores.size()) {
				k = myAgent.receive(mt);
				if (k == null)
					block();
				else {
					try {
						Action a = (Action) getContentManager().extractContent(k);
						Ofertar o = (Ofertar) a.getAction();
						System.out.println("Oferta recibida: " + o.getOferta().getPrecio() + "€ de " + a.getActor().getLocalName());
					} catch (CodecException | OntologyException e) {
						e.printStackTrace();
					} finally {
						count++;
					}
				}
			}

			myAgent.doDelete();
		}
	}

	@Override
	protected void setup() {
		super.setup();

		// busqueda en paginas amarillas de las tiendas
		vendedores = new ArrayList<>();
		DFAgentDescription busqueda = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("tienda");
		busqueda.addServices(sd);
		try {
			DFAgentDescription[] dfad = DFService.search(this, busqueda);
			for (DFAgentDescription g : dfad)
				vendedores.add(g.getName());
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		codec = new SLCodec();
		onto = OntologiaVinilos.getInstance();
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(onto);
		CompraBehaviour c = new CompraBehaviour();
		this.addBehaviour(c);

	}
}
