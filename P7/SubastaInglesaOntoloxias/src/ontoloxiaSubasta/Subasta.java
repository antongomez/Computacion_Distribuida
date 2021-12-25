package ontoloxiaSubasta;

/**
 * Protege name: Subasta
 *
 * @author OntologyBeanGenerator v4.1
 * @version 2021/12/25, 21:18:31
 */
public interface Subasta extends jade.content.Concept {

    public static final String ESTADO_0 = "Buscando compradores";
    public static final String ESTADO_1 = "En curso";
    public static final String ESTADO_2 = "Finalizada";

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
