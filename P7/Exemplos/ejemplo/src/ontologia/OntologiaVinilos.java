package com.distribuida.onto.ejemploOntologia.ontologia;

import jade.content.onto.*;
import jade.content.schema.*;

public class OntologiaVinilos extends jade.content.onto.Ontology {

	public static final String ONTOLOGY_NAME = "OntologiaVinilos";

	// Instancia singleton de la ontología
	private static Ontology theInstance = new OntologiaVinilos();

	public static Ontology getInstance() {
		return theInstance;
	}

	// Vocabulario
	public static final String PEDIR_VINILO = "vinilo";
	public static final String PEDIR = "Pedir";
	public static final String OFERTAR_OFERTA = "oferta";
	public static final String OFERTAR = "Ofertar";
	public static final String VINILO_ARTISTA = "artista";
	public static final String VINILO_TITULO = "titulo";
	public static final String VINILO = "Vinilo";
	public static final String OFERTA_PRECIO = "precio";
	public static final String OFERTA_PRODUCTO = "producto";
	public static final String OFERTA = "Oferta";

	private OntologiaVinilos() {
		super(ONTOLOGY_NAME, BasicOntology.getInstance());
		try {

			ConceptSchema ofertaSchema = new ConceptSchema(OFERTA);
			add(ofertaSchema, Oferta.class);
			ConceptSchema viniloSchema = new ConceptSchema(VINILO);
			add(viniloSchema, Vinilo.class);

			AgentActionSchema ofertarSchema = new AgentActionSchema(OFERTAR);
			add(ofertarSchema, Ofertar.class);
			AgentActionSchema pedirSchema = new AgentActionSchema(PEDIR);
			add(pedirSchema, Pedir.class);

			// Añadir slots
			ofertaSchema.add(OFERTA_PRODUCTO, viniloSchema, ObjectSchema.MANDATORY);
			ofertaSchema.add(OFERTA_PRECIO, (PrimitiveSchema) getSchema(BasicOntology.FLOAT), ObjectSchema.MANDATORY);

			viniloSchema.add(VINILO_TITULO, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
			viniloSchema.add(VINILO_ARTISTA, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);

			ofertarSchema.add(OFERTAR_OFERTA, ofertaSchema, ObjectSchema.OPTIONAL);
			pedirSchema.add(PEDIR_VINILO, viniloSchema, ObjectSchema.MANDATORY);

		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}
}
