package com.distribuida.onto.ejemploOntologia.ontologia;

public class Vinilo implements jade.content.Concept {

	private String titulo;
	private String artista;

	public Vinilo() {
		titulo = "";
		artista = "";
	}

	public void setTitulo(String value) {
		this.titulo = value;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setArtista(String value) {
		this.artista = value;
	}

	public String getArtista() {
		return this.artista;
	}

}
