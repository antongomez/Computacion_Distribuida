package com.distribuida.onto.ejemploOntologia.ontologia;

public class Pedir implements jade.content.AgentAction {

	private Vinilo vinilo;

	public Pedir() {
		vinilo = new Vinilo();
	}

	public void setVinilo(Vinilo value) {
		this.vinilo = value;
	}

	public Vinilo getVinilo() {
		return this.vinilo;
	}

}
