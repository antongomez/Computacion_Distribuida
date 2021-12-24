package ontoloxiaSubasta.impl;

import ontoloxiaSubasta.Subasta;

/**
* Protege name: Subasta
* @author OntologyBeanGenerator v4.1
* @version 2021/12/24, 17:59:57
*/
public class DefaultSubasta implements Subasta {

  private static final long serialVersionUID = 5385195621906642128L;

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
   private String ganhador;
   public void setGanhador(String value) { 
    this.ganhador=value;
   }
   public String getGanhador() {
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
