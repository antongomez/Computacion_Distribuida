package alertas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Anton Gomez Lopez
 */
public class Alerta implements Serializable {

    private final TipoAlerta tipo;
    private final String empresa;
    private final float valor;
    private final String nomeCola;

    public Alerta(TipoAlerta tipo, String empresa, float valor, String nomeCola) {
        this.tipo = tipo;
        this.empresa = empresa;
        this.valor = valor;
        this.nomeCola = nomeCola;
    }

    public boolean cumpreseAlerta(int valorLido) {
        if (tipo.compareTo(TipoAlerta.COMPRA) == 0) {
            return (valorLido < this.valor);
        } else {
            return (valorLido > this.valor);
        }
    }

    public static HashMap<String, List<Alerta>> clasificarEmpresas(List<Alerta> alertas) {
        HashMap<String, List<Alerta>> clasificacion = new HashMap<>();

        List<Alerta> lista;
        // Percorremos as alertas e para cada unha, metemola nunha lista
        // onde todas as alertas da lista estan asociadas a unha mesma empresa
        for (Alerta a : alertas) {
            // Obtemos a lista coas alertas que estan asociadas a empresa 
            // da alerta que estamos clasificando
            lista = clasificacion.get(a.getEmpresa());
            // En caso de que ainda non existisen alertas sobre esa empresa
            // creamos unha lista
            if (lista == null) {
                lista = new ArrayList<>();
            }
            lista.add(a);
            // Engadimos a lista coa alerta adicional ao hashmap
        }

        return clasificacion;
    }

    public static List<Alerta> obterAlertasEmpresa(String empresa, List<Alerta> alertas) {
        return clasificarEmpresas(alertas).get(empresa);
    }

    public TipoAlerta getTipo() {
        return tipo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public float getValor() {
        return valor;
    }

    public String getNomeCola() {
        return nomeCola;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.tipo);
        hash = 73 * hash + Objects.hashCode(this.empresa);
        hash = 73 * hash + Float.floatToIntBits(this.valor);
        hash = 73 * hash + Objects.hashCode(this.nomeCola);
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
        final Alerta other = (Alerta) obj;
        if (Float.floatToIntBits(this.valor) != Float.floatToIntBits(other.valor)) {
            return false;
        }
        if (!Objects.equals(this.empresa, other.empresa)) {
            return false;
        }
        if (!Objects.equals(this.nomeCola, other.nomeCola)) {
            return false;
        }
        if (this.tipo != other.tipo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Alerta{" + "tipo=" + tipo + ", empresa=" + empresa + ", valor=" + valor + ", nomeCola=" + nomeCola + '}';
    }

}
