package com.distribuida.onto.ejemploOntologia.ontologia;

public class Ofertar implements jade.content.AgentAction {

	private Oferta oferta;

	public Ofertar() {
		oferta = new Oferta();
	}

	public void setOferta(Oferta value) {
		this.oferta = value;
	}

	public Oferta getOferta() {
		return this.oferta;
	}
}
