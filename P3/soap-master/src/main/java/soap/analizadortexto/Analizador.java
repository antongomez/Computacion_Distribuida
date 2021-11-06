package soap.analizadortexto;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService
public interface Analizador {

    @WebMethod(operationName = "contarpalabras")
    @WebResult(name = "numeropalabras")
    int contar_palabras(@WebParam(name = "cadea") String cadea);

    @WebMethod(operationName = "contarcaracteres")
    @WebResult(name = "numerocaracteres")
    int contar_caracteres(@WebParam(name = "cadea") String cadea);

    @WebMethod(operationName = "contarfrases")
    @WebResult(name = "numerofrases")
    int contar_frases(@WebParam(name = "cadea") String cadea);

    @WebMethod(operationName = "repeticionpalabra")
    @WebResult(name = "numeropalabra")
    int contar_palabra(@WebParam(name = "cadea") String cadea, @WebParam(name = "palabra") String palabra);

    @WebMethod(operationName = "palabramaisusada")
    @WebResult(name = "palabra")
    List<String> pal_mais_usada(@WebParam(name = "cadea") String cadea);

    @WebMethod(operationName = "palabramenosusada")
    @WebResult(name = "palabra")
    List<String> pal_menos_usada(@WebParam(name = "cadea") String cadea);

    @WebMethod(operationName = "reemprazarpalabra")
    @WebResult(name = "frase")
    String subs_palabra(@WebParam(name = "cadea") String cadea, @WebParam(name = "palabra_antiga") String palabra_antiga, @WebParam(name = "palabra_nova") String palabra_nova);

}
