package mensaxe;

import alertas.Alerta;
import java.io.Serializable;

/**
 *
 * @author Anton Gomez Lopez
 */
public class Mensaxe implements Serializable {

    private TipoMensaxe tipo;
    private Alerta alerta;
    private String info;

    // Para ponher alertas
    public Mensaxe(TipoMensaxe tipo, Alerta alerta) {
        this.tipo = tipo;
        this.alerta = alerta;
        this.info = null;
    }

    // Para saudos
    public Mensaxe(TipoMensaxe tipo, String info) {
        this.tipo = tipo;
        this.alerta = null;
        this.info = info;
    }

    // Para notificar alertas
    public Mensaxe(TipoMensaxe tipo, Alerta alerta, String info) {
        this.tipo = tipo;
        this.alerta = alerta;
        this.info = info;
    }

    public TipoMensaxe getTipo() {
        return tipo;
    }

    public void setTipo(TipoMensaxe tipo) {
        this.tipo = tipo;
    }

    public Alerta getAlerta() {
        return alerta;
    }

    public void setAlerta(Alerta alerta) {
        this.alerta = alerta;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        if (tipo.compareTo(TipoMensaxe.SAUDO) == 0) {
            return "Mensaxe{" + "tipo=" + tipo.name() + ", info=" + info + '}';
        } else if (tipo.compareTo(TipoMensaxe.NOTIFICAR_ALERTA) == 0) {
            return "Mensaxe{" + "tipo=" + tipo.name() + ", alerta=" + alerta + '}';
        } else if (tipo.compareTo(TipoMensaxe.DESPEDIDA) == 0) {
            return "Mensaxe{" + "tipo=" + tipo.name() + ", info=" + info + '}';
        } else {
            return "Mensaxe{" + "tipo=" + tipo.name() + ", alerta=" + alerta + '}' + ", info=" + info + '}';
        }
    }

}
