package ontoloxiaSubasta.impl;

import ontoloxiaSubasta.*;

/**
 * Protege name: Interesa
 *
 * @author OntologyBeanGenerator v4.1
 * @version 2022/01/14, 16:46:30
 */
public class DefaultInteresa implements Interesa {

    private static final long serialVersionUID = 3590751218202108299L;

    private String _internalInstanceName = null;

    public DefaultInteresa() {
        this._internalInstanceName = "";
    }

    public DefaultInteresa(String instance_name) {
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
        this.subasta = value;
    }

    public Subasta getSubasta() {
        return this.subasta;
    }

}
