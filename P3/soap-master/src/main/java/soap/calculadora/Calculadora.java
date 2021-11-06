package soap.calculadora;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService
public interface Calculadora {
    @WebMethod(operationName = "suma")
    @WebResult(name = "resultado")
    int suma(@WebParam(name = "sumando1") int a, @WebParam(name = "sumando2") int b);
    
    @WebMethod(operationName = "resta")
    @WebResult(name = "resultado")
    int resta(@WebParam(name = "minuendo") int a, @WebParam(name = "sustraendo") int b);
    
    @WebMethod(operationName = "multiplicacion")
    @WebResult(name = "resultado")
    int mult(@WebParam(name = "factor1") int a, @WebParam(name = "factor2") int b);
    
    @WebMethod(operationName = "division")
    @WebResult(name = "resultado")
    double div(@WebParam(name = "dividendo") int a, @WebParam(name = "divisor") int b);
    
    @WebMethod(operationName = "potencia")
    @WebResult(name = "resultado")
    double pot(@WebParam(name = "base") int a, @WebParam(name = "exponhente") int b);
    
    @WebMethod(operationName = "raiz")
    @WebResult(name = "resultado")
    double raiz_cadrada(@WebParam(name = "base") int a);
    
    @WebMethod(operationName = "logaritmo")
    @WebResult(name = "resultado")
    double ln(@WebParam(name = "valor") int a);
    
    @WebMethod(operationName = "maximo")
    @WebResult(name = "resultado")
    int max(@WebParam(name = "lista") int[] nums);
    
    @WebMethod(operationName = "minimo")
    @WebResult(name = "resultado")
    int min(@WebParam(name = "lista") int[] nums);
    
    @WebMethod(operationName = "media")
    @WebResult(name = "resultado")
    double media(@WebParam(name = "lista") int[] nums);
    
    @WebMethod(operationName = "mediana")
    @WebResult(name = "resultado")
    float mediana(@WebParam(name = "lista") int[] nums);
    
    @WebMethod(operationName = "moda")
    @WebResult(name = "resultado")
    List<Integer> moda(@WebParam(name = "lista") int[] nums);
    
    @WebMethod(operationName = "desviacion")
    @WebResult(name = "resultado")
    double desv(@WebParam(name = "lista") int[] nums);
    
    
}
