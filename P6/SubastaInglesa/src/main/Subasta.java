package main;

import java.util.Objects;

/**
 *
 * @author Anton Gomez Lopez
 */
public class Subasta {

    private String idSubasta;
    private String titulo;
    private Integer incremento;
    private Float prezoActual;
    private Integer ronda;
    private String ganhador;
    private String estado;

    public static final String ESTADO_0 = "Buscando compradores";
    public static final String ESTADO_1 = "En curso";
    public static final String ESTADO_2 = "Finalizada";

    public Subasta(String idSubasta, String titulo, Integer incremento, float prezoActual, int ronda, String ganhador) {
        this.idSubasta = idSubasta;
        this.titulo = titulo;
        this.incremento = incremento;
        this.prezoActual = prezoActual;
        this.ronda = ronda;
        this.ganhador = ganhador;
        this.estado = ESTADO_0;
    }
    
    public Subasta(String idSubasta, String titulo, float prezoActual, int ronda, String ganhador) {
        this.idSubasta = idSubasta;
        this.titulo = titulo;
        this.incremento = incremento;
        this.prezoActual = prezoActual;
        this.ronda = ronda;
        this.ganhador = ganhador;
        this.estado = ESTADO_0;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public float getPrezoActual() {
        return prezoActual;
    }

    public void setPrezoActual(float prezoActual) {
        this.prezoActual = prezoActual;
    }

    public int getRonda() {
        return ronda;
    }

    public void setRonda(int ronda) {
        this.ronda = ronda;
    }

    public String getGanhador() {
        return ganhador;
    }

    public void setGanhador(String ganhador) {
        this.ganhador = ganhador;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdSubasta() {
        return idSubasta;
    }

    public void setIdSubasta(String idSubasta) {
        this.idSubasta = idSubasta;
    }

    public Integer getIncremento() {
        return incremento;
    }

    public void setIncremento(Integer incremento) {
        this.incremento = incremento;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.titulo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Subasta other = (Subasta) obj;
        if (!Objects.equals(this.titulo, other.titulo)) {
            return false;
        }
        return true;
    }

}
