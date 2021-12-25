package ontoloxiaSubasta.impl;


import ontoloxiaSubasta.*;

/**
* Protege name: Informar
* @author OntologyBeanGenerator v4.1
* @version 2021/12/25, 21:18:31
*/
public class DefaultInformar implements Informar {

  private static final long serialVersionUID = -9065970472412463086L;

  private String _internalInstanceName = null;

  public DefaultInformar() {
    this._internalInstanceName = "";
  }

  public DefaultInformar(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: subasta
   */
   private Subasta subasta;
   public void setSubasta(Subasta value) { 
    this.subasta=value;
   }
   public Subasta getSubasta() {
     return this.subasta;
   }

}
