package soap.analizadortexto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import javax.jws.WebService;

@WebService(
        endpointInterface = "soap.analizadortexto.Analizador",
        serviceName = "Analizador"
)
public class AnalizadorImpl implements Analizador {

    @Override
    public int contar_palabras(String cadea) {
        return cadea.split(" ").length;
    }

    @Override
    public int contar_caracteres(String cadea) {
        return cadea.split("").length;
    }

    @Override
    public int contar_frases(String cadea) {
        return cadea.split(Pattern.quote(".")).length;
    }

    @Override
    public int contar_palabra(String cadea, String palabra) {
        String[] palabras = eliminarSignosPuntuacion(cadea).toLowerCase().split(" ");
        int conta = 0;

        for (int i = 0; i < palabras.length; i++) {
            if (palabras[i].equals(palabra)) {
                conta++;
            }
        }

        return conta;
    }

    @Override
    public List<String> pal_mais_usada(String cadea) {
        // Contamos as veces que aparece cada palabra
        HashMap<String, Integer> repPalabra = contarPalabras(cadea);

        // Calculamos o minimo
        ArrayList<Integer> valores = new ArrayList<>(repPalabra.values());
        Collections.sort(valores);
        int max = valores.get(valores.size() - 1);

        // Obtemos as palabras que se repiten min veces
        ArrayList<String> palabrasMaisUsadas = new ArrayList<>();
        for (String str : repPalabra.keySet()) {
            if (repPalabra.get(str) == max) {
                palabrasMaisUsadas.add(str);
            }
        }

        return palabrasMaisUsadas;
    }

    @Override
    public List<String> pal_menos_usada(String cadea) {
        // Contamos as veces que aparece cada palabra
        HashMap<String, Integer> repPalabra = contarPalabras(cadea);

        // Calculamos o minimo
        ArrayList<Integer> valores = new ArrayList<>(repPalabra.values());
        Collections.sort(valores);
        int min = valores.get(0);

        // Obtemos as palabras que se repiten min veces
        ArrayList<String> palabrasMenosUsadas = new ArrayList<>();
        for (String str : repPalabra.keySet()) {
            if (repPalabra.get(str) == min) {
                palabrasMenosUsadas.add(str);
            }
        }

        return palabrasMenosUsadas;
    }

    @Override
    public String subs_palabra(String cadea, String palabra_antiga, String palabra_nova) {
        return cadea.replaceAll(palabra_antiga, palabra_nova);
    }

    private HashMap<String, Integer> contarPalabras(String cadea) {
        String cadeaSenSignos = eliminarSignosPuntuacion(cadea).toLowerCase();
        String[] palabras = cadeaSenSignos.split(" ");
        HashMap<String, Integer> repPalabra = new HashMap<>();

        // Contamos as veces que aparece cada palabra
        for (int i = 0; i < palabras.length; i++) {
            repPalabra.put(palabras[i], contar_palabra(cadeaSenSignos, palabras[i]));
        }

        return repPalabra;
    }

    private String eliminarSignosPuntuacion(String cadea) {
        String novaCadea = "";
        String regex = ";,.:?¿¡!";
        for (int i = 0; i < cadea.length(); i++) {
            String c = Character.toString(cadea.charAt(i));
            if (!regex.contains(c)) {
                novaCadea += c;
            }
        }

        return novaCadea;
    }
}
