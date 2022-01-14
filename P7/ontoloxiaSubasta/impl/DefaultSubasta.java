package ontoloxiaSubasta.impl;


import ontoloxiaSubasta.*;

/**
* Información necesaria dunha subasta dun libro
* Protege name: Subasta
* @author OntologyBeanGenerator v4.1
* @version 2022/01/14, 16:46:30
*/
public class DefaultSubasta implements Subasta {

  private static final long serialVersionUID = 3590751218202108299L;

  private String _internalInstanceName = null;

  public DefaultSubasta() {
    this._internalInstanceName = "";
  }

  public DefaultSubasta(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: incremento
   */
   private int incremento;
   public void setIncremento(int value) { 
    this.incremento=value;
   }
   public int getIncremento() {
     return this.incremento;
   }

   /**
   * Protege name: ronda
   */
   private int ronda;
   public void setRonda(int value) { 
    this.ronda=value;
   }
   public int getRonda() {
     return this.ronda;
   }

   /**
   * Protege name: titulo
   */
   private String titulo;
   public void setTitulo(String value) { 
    this.titulo=value;
   }
   public String getTitulo() {
     return this.titulo;
   }

   /**
   * Protege name: estado
   */
   private String estado;
   public void setEstado(String value) { 
    this.estado=value;
   }
   public String getEstado() {
     return this.estado;
   }

   /**
   * Protege name: ganhador
   */
   private jade.core.AID ganhador;
   public void setGanhador(jade.core.AID value) { 
    this.ganhador=value;
   }
   public jade.core.AID getGanhador() {
     return this.ganhador;
   }

   /**
   * Protege name: idSubasta
   */
   private String idSubasta;
   public void setIdSubasta(String value) { 
    this.idSubasta=value;
   }
   public String getIdSubasta() {
     return this.idSubasta;
   }

   /**
   * Protege name: prezo
   */
   private float prezo;
   public void setPrezo(float value) { 
    this.prezo=value;
   }
   public float getPrezo() {
     return this.prezo;
   }

}
