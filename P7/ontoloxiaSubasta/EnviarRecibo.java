package ontoloxiaSubasta;



/**
* Protege name: enviarRecibo
* @author OntologyBeanGenerator v4.1
* @version 2021/12/25, 21:18:31
*/
public interface EnviarRecibo extends jade.content.AgentAction {

   /**
   * Protege name: comprador
   */
   public void setComprador(jade.core.AID value);
   public jade.core.AID getComprador();

   /**
   * Protege name: titulo
   */
   public void setTitulo(String value);
   public String getTitulo();

   /**
   * Protege name: vendedor
   */
   public void setVendedor(jade.core.AID value);
   public jade.core.AID getVendedor();

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
