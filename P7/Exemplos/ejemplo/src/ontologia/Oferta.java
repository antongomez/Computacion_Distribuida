package com.distribuida.onto.ejemploOntologia.ontologia;

public class Oferta implements jade.content.Concept {

	private Vinilo producto;
	private float precio;

	public Oferta() {
		producto = new Vinilo();
		precio = 0;
	}

	public void setProducto(Vinilo value) {
		this.producto = value;
	}

	public Vinilo getProducto() {
		return this.producto;
	}

	public void setPrecio(float value) {
		this.precio = value;
	}

	public float getPrecio() {
		return this.precio;
	}

}
