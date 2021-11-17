package servidor;

import alertas.Alerta;
import alertas.TipoAlerta;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import gui.controlador.ControladorAlertas;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mensaxe.Mensaxe;
import mensaxe.TipoMensaxe;

/**
 *
 * @author Anton Gomez Lopez
 */
public class Servidor {

    public static final String URL = "https://www.bolsamadrid.es/esp/aspx/Mercados/Precios.aspx?indice=ESI100000000&punto=indice";
    private static final String COLA_SERVIDOR = "servidor";

    public static void main(String args[]) throws IOException {

        // Creamos o vector de alertas
        ArrayList<Alerta> alertas = new ArrayList<>();

        // Enviamos a informacion
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection conexion = factory.newConnection();
            Channel canal = conexion.createChannel();

            // Declaramos a cola do servidor
            canal.queueDeclare(COLA_SERVIDOR, false, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                ByteArrayInputStream bs = new ByteArrayInputStream(delivery.getBody());
                try (ObjectInputStream is = new ObjectInputStream(bs)) {
                    try {
                        Mensaxe m = (Mensaxe) is.readObject();
                        System.out.println(m);
                        interpretarMensaxe(m, canal, alertas);
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            canal.basicConsume(COLA_SERVIDOR, true, deliverCallback, consumerTag -> {
            });

            // Creamos o fio que se encarga de recibir as mensaxes na cola do
            // servidor
            HashMap<String, Float> valoresActuais;
            while (true) {
                System.out.println("Comprobando alertas");
                // Comprobo se me da un 200 ao facer a peticion
                if (getStatusConnectionCode(URL) == 200) {
                    valoresActuais = obterValores();
                    comprobarAlertas(valoresActuais, alertas, canal);
                } else {
                    System.out.println("Status Code distinto de 200");
                }
                // Cada minuto consultamos a paxina
                try {
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } catch (TimeoutException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Con este metodo comprobo o Status code da resposta que recibo ao facer a
     * peticion EX:
     *
     * 200 OK 300 Multiple Choices 301 Moved Permanently 305 Use Proxy 400 Bad
     * Request 403 Forbidden 404 Not Found 500 Internal Server Error 502 Bad
     * Gateway 503 Service Unavailable
     *
     * @param url
     * @return Status Code
     */
    private static int getStatusConnectionCode(String url) {

        Response response = null;

        try {
            response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
        } catch (IOException ex) {
            System.out.println("Excepcion ao obter o Status Code: " + ex.getMessage());
        }
        return response.statusCode();
    }

    /**
     * Con este metodo devolvo un obxecto da clase Document co contido do HTML
     * da web que me permitira parsealo cos metodos da librelia JSoup
     *
     * @param url
     * @return Documento con el HTML
     */
    private static Document getHtmlDocument(String url) {

        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
        } catch (IOException ex) {
            System.out.println("Excepcion ao obter o HTML da paxina: " + ex.getMessage());
        }
        return doc;
    }

    protected static String obterNomesEmpresas() {

        // Obtenho o HTML da web nun obxecto Document
        Document documento = getHtmlDocument(URL);

        // Taboa onde estan os valores
        Element taboa = documento.getElementById("ctl00_Contenido_tblAcciones");
        String mensaxe = "";
        // Obtemos os nomes das empresas
        for (Element linha : taboa.select("tr")) {
            Elements celdas = linha.select("td");
            if (celdas.size() > 0) {
                mensaxe += linha.select("td").get(0).text() + "/";
            }
        }

        return mensaxe;
    }

    private static void interpretarMensaxe(Mensaxe m, Channel canal, ArrayList<Alerta> alertas) {
        switch (m.getTipo()) {
            case SAUDO -> {
                // Enviamos un saudo ao servidor para que nos asigne un canal
                Mensaxe men = new Mensaxe(TipoMensaxe.SAUDO, Servidor.obterNomesEmpresas());
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                try {
                    // Declaramos a cola do servidor
                    canal.queueDeclare(m.getInfo(), false, false, true, null);
                    try (ObjectOutputStream os = new ObjectOutputStream(bs)) {
                        os.writeObject(men);
                        canal.basicPublish("", m.getInfo(), null, bs.toByteArray());
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ControladorAlertas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            case PONHER_ALERTA -> {
                // Evitamos que os fios do servidor interfiran un no outro
                synchronized (alertas) {
                    alertas.add(m.getAlerta());
                }
            }
            case DESPEDIDA -> {
                // Evitamos que os fios do servidor interfiran un no outro
                synchronized (alertas) {
                    // Eliminamos as alertas do cliente que se acaba de despedir
                    ArrayList<Alerta> als = new ArrayList<>(alertas);
                    for (Alerta a : als) {
                        if (a.getNomeCola().equals(m.getInfo())) {
                            alertas.remove(a);
                        }
                    }
                }
            }
        }
    }

    private static HashMap<String, Float> obterValores() {
        HashMap<String, Float> valoresActuais = new HashMap<>();

        Document documento = getHtmlDocument(URL);

        // Taboa onde estan os valores
        Element taboa = documento.getElementById("ctl00_Contenido_tblAcciones");
        String empresa;
        Float valor;
        for (Element linha : taboa.select("tr")) {
            Elements celdas = linha.select("td");
            if (celdas.size() > 0) {
                empresa = linha.select("td").get(0).text();
                valor = Float.parseFloat(linha.select("td").get(1).text().replace(".", "").replace(",", "."));
                valoresActuais.put(empresa, valor);
            }
        }

        return valoresActuais;
    }

    private static void comprobarAlertas(HashMap<String, Float> valoresActuais, ArrayList<Alerta> alertas, Channel canal) {
        // Mentres o fio do servidor que comproba as alertas realzia a sua
        // tarefa, o fio que recibe alertas non pode escrbir o vector de 
        // alertas
        Float valorActual;
        synchronized (alertas) {
            // Creamos un arrayList de alertas para ir gardando as alertas 
            // que imos eliminar
            ArrayList<Alerta> alertasEliminar = new ArrayList<>();
            for (Alerta a : alertas) {
                valorActual = valoresActuais.get(a.getEmpresa());
                if ((a.getTipo().compareTo(TipoAlerta.VENTA) == 0) && (valorActual > a.getValor())) {

                    enviarAlerta(canal, a, valorActual);
                    alertasEliminar.add(a);

                } else if ((a.getTipo().compareTo(TipoAlerta.COMPRA) == 0) && (valorActual < a.getValor())) {

                    enviarAlerta(canal, a, valorActual);
                    alertasEliminar.add(a);
                }
            }

            // Unha vez que percorrimos as alertas, eliminamos as que se 
            // cumpriron
            alertasEliminar.forEach(a -> {
                alertas.remove(a);
            });
        }

    }

    private static void enviarAlerta(Channel canal, Alerta a, Float valorActual) {
        Mensaxe men = new Mensaxe(TipoMensaxe.NOTIFICAR_ALERTA, a, valorActual.toString());
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        try {

            try (ObjectOutputStream os = new ObjectOutputStream(bs)) {
                os.writeObject(men);
                canal.basicPublish("", a.getNomeCola(), null, bs.toByteArray());
            }
        } catch (IOException ex) {
            System.out.println("Erro enviando alerta de notificacion");
            Logger.getLogger(ControladorAlertas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
