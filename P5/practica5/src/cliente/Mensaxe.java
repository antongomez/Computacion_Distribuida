package cliente;

public class Mensaxe {

    private String msx;
    private String destinatario;
    private String emisor;

    public Mensaxe(String msx, String destinatario, String emisor) {
        this.msx = msx;
        this.destinatario = destinatario;
        this.emisor = emisor;
    }

    public String getMsx() {
        return msx;
    }

    public void setMsx(String msx) {
        this.msx = msx;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

}
