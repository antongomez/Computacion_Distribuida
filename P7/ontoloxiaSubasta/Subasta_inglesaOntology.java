// file: Subasta_inglesaOntology.java generated by ontology bean generator.  DO NOT EDIT, UNLESS YOU ARE REALLY SURE WHAT YOU ARE DOING!
package ontoloxiaSubasta;

import jade.content.onto.*;
import jade.content.schema.*;

/** file: Subasta_inglesaOntology.java
 * @author OntologyBeanGenerator v4.1
 * @version 2021/12/24, 17:59:57
 */
public class Subasta_inglesaOntology extends jade.content.onto.Ontology  {

  private static final long serialVersionUID = 5385195621906642128L;

  //NAME
  public static final String ONTOLOGY_NAME = "subasta-inglesa";
  // The singleton instance of this ontology
  private static Ontology theInstance = new Subasta_inglesaOntology();
  public static Ontology getInstance() {
     return theInstance;
  }


   // VOCABULARY
    public static final String INTERESA_SUBASTA="subasta";
    public static final String INTERESA="Interesa";
    public static final String ENVIARRECIBO_PREZO="prezo";
    public static final String ENVIARRECIBO_IDSUBASTA="idSubasta";
    public static final String ENVIARRECIBO_VENDEDOR="vendedor";
    public static final String ENVIARRECIBO_TITULO="titulo";
    public static final String ENVIARRECIBO_COMPRADOR="comprador";
    public static final String ENVIARRECIBO="EnviarRecibo";
    public static final String INFORMAR_SUBASTA="subasta";
    public static final String INFORMAR="Informar";
    public static final String SUBASTA_PREZO="prezo";
    public static final String SUBASTA_IDSUBASTA="idSubasta";
    public static final String SUBASTA_GANHADOR="ganhador";
    public static final String SUBASTA_ESTADO="estado";
    public static final String SUBASTA_TITULO="titulo";
    public static final String SUBASTA_RONDA="ronda";
    public static final String SUBASTA_INCREMENTO="incremento";
    public static final String SUBASTA="Subasta";

  /**
   * Constructor
  */
  private Subasta_inglesaOntology(){ 
    super(ONTOLOGY_NAME, BasicOntology.getInstance());
    try { 

    // adding Concept(s)
    ConceptSchema subastaSchema = new ConceptSchema(SUBASTA);
    add(subastaSchema, ontoloxiaSubasta.Subasta.class);

    // adding AgentAction(s)
    AgentActionSchema informarSchema = new AgentActionSchema(INFORMAR);
    add(informarSchema, ontoloxiaSubasta.Informar.class);
    AgentActionSchema enviarReciboSchema = new AgentActionSchema(ENVIARRECIBO);
    add(enviarReciboSchema, ontoloxiaSubasta.EnviarRecibo.class);

    // adding AID(s)

    // adding Predicate(s)
    PredicateSchema interesaSchema = new PredicateSchema(INTERESA);
    add(interesaSchema, ontoloxiaSubasta.Interesa.class);


    // adding fields
    subastaSchema.add(SUBASTA_INCREMENTO, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    subastaSchema.add(SUBASTA_RONDA, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    subastaSchema.add(SUBASTA_TITULO, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    subastaSchema.add(SUBASTA_ESTADO, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    subastaSchema.add(SUBASTA_GANHADOR, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    subastaSchema.add(SUBASTA_IDSUBASTA, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    subastaSchema.add(SUBASTA_PREZO, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.MANDATORY);
    informarSchema.add(INFORMAR_SUBASTA, subastaSchema, ObjectSchema.MANDATORY);
    enviarReciboSchema.add(ENVIARRECIBO_COMPRADOR, (ConceptSchema)getSchema(BasicOntology.AID), ObjectSchema.MANDATORY);
    enviarReciboSchema.add(ENVIARRECIBO_TITULO, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    enviarReciboSchema.add(ENVIARRECIBO_VENDEDOR, (ConceptSchema)getSchema(BasicOntology.AID), ObjectSchema.MANDATORY);
    enviarReciboSchema.add(ENVIARRECIBO_IDSUBASTA, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    enviarReciboSchema.add(ENVIARRECIBO_PREZO, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.MANDATORY);
    interesaSchema.add(INTERESA_SUBASTA, subastaSchema, ObjectSchema.MANDATORY);

    // adding name mappings

    // adding inheritance

   }catch (java.lang.Exception e) {e.printStackTrace();}
  }
}
