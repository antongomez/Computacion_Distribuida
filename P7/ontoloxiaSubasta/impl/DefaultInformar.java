package ontoloxiaSubasta.impl;


import ontoloxiaSubasta.*;

/**
* Informase ao comprador do estado da subasta
* Protege name: Informar
* @author OntologyBeanGenerator v4.1
* @version 2022/01/14, 16:46:30
*/
public class DefaultInformar implements Informar {

  private static final long serialVersionUID = 3590751218202108299L;

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
