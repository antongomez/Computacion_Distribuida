package ontoloxiaSubasta;



/**
* Información necesaria dunha subasta dun libro
* Protege name: Subasta
* @author OntologyBeanGenerator v4.1
* @version 2022/01/14, 16:46:30
*/
public interface Subasta extends jade.content.Concept {

   /**
   * Protege name: incremento
   */
   public void setIncremento(int value);
   public int getIncremento();

   /**
   * Protege name: ronda
   */
   public void setRonda(int value);
   public int getRonda();

   /**
   * Protege name: titulo
   */
   public void setTitulo(String value);
   public String getTitulo();

   /**
   * Protege name: estado
   */
   public void setEstado(String value);
   public String getEstado();

   /**
   * Protege name: ganhador
   */
   public void setGanhador(jade.core.AID value);
   public jade.core.AID getGanhador();

   /**
   * Protege name: idSubasta
   */
   public void setIdSubasta(String value);
   public String getIdSubasta();

   /**
   * Protege name: prezo
   */
   public void setPrezo(float value);
   public float getPrezo();

}
