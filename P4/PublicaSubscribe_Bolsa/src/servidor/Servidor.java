package servidor;

import java.io.IOException;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Anton Gomez Lopez
 */
public class Servidor {

    public static final String url = "https://www.bolsamadrid.es/esp/aspx/Mercados/Precios.aspx?indice=ESI100000000&punto=indice";

    public static void main(String args[]) {

        // Comprobo se me da un 200 ao facer a peticion
        if (getStatusConnectionCode(url) == 200) {

            // Obtenho o HTML da web nun obxecto Document
            Document documento = getHtmlDocument(url);

            // Taboa onde estan os valores
            Element taboa = documento.select("table").get(4);
            
            // Obtemos os nomes das empresas
            for (Element linha : taboa.select("tr")) {
                Elements celdas = linha.select("td");
                if (celdas.size() > 0) {
                    String c = linha.select("td").get(0).text();
                    System.out.println(c);
                }

            }

        } else {
            System.out.println("El Status Code no es OK es: " + getStatusConnectionCode(url));
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
    public static int getStatusConnectionCode(String url) {

        Response response = null;

        try {
            response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
        } catch (IOException ex) {
            System.out.println("Excepción al obtener el Status Code: " + ex.getMessage());
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
    public static Document getHtmlDocument(String url) {

        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
        } catch (IOException ex) {
            System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
        }
        return doc;
    }

}
