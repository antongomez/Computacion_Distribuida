package soap;

import soap.calculadora.CalculadoraImpl;
import javax.xml.ws.Endpoint;
import soap.analizadortexto.Analizador;
import soap.analizadortexto.AnalizadorImpl;
import soap.calculadora.Calculadora;

public class Servidor {
    public static void main(String[] args) {
        Calculadora c = new CalculadoraImpl();
        String address = "http://localhost:8080/calculadora";
        Endpoint.publish(address, c);
        
        Analizador a = new AnalizadorImpl();
        address = "http://localhost:8080/analizador";
        Endpoint.publish(address, a);
    }
}
