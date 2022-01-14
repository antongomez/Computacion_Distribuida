package ontoloxiaSubasta.impl;


import ontoloxiaSubasta.*;

/**
* Enviaselle ao comprador o recibo da compra
* Protege name: enviarRecibo
* @author OntologyBeanGenerator v4.1
* @version 2022/01/14, 16:46:30
*/
public class DefaultEnviarRecibo implements EnviarRecibo {

  private static final long serialVersionUID = 3590751218202108299L;

  private String _internalInstanceName = null;

  public DefaultEnviarRecibo() {
    this._internalInstanceName = "";
  }

  public DefaultEnviarRecibo(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: comprador
   */
   private jade.core.AID comprador;
   public void setComprador(jade.core.AID value) { 
    this.comprador=value;
   }
   public jade.core.AID getComprador() {
     return this.comprador;
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
   * Protege name: vendedor
   */
   private jade.core.AID vendedor;
   public void setVendedor(jade.core.AID value) { 
    this.vendedor=value;
   }
   public jade.core.AID getVendedor() {
     return this.vendedor;
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
