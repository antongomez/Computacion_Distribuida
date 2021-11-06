package soap;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import soap.analizadortexto.Analizador;
import soap.calculadora.Calculadora;

public class Cliente {

    public static void main(String[] args) throws MalformedURLException {
        URL wsdlURL = new URL("http://localhost:8080/calculadora?wsdl");
        QName SERVICE_NAME = new QName("http://calculadora.soap/", "Calculadora");
        Service servizo = Service.create(wsdlURL, SERVICE_NAME);
        Calculadora calculadora = servizo.getPort(Calculadora.class);

        wsdlURL = new URL("http://localhost:8080/analizador?wsdl");
        SERVICE_NAME = new QName("http://analizadortexto.soap/", "Analizador");
        servizo = Service.create(wsdlURL, SERVICE_NAME);
        Analizador analizador = servizo.getPort(Analizador.class);

        String op = "";

        String menu = "1. Calculadora\n"
                + "2. Analizador de texto\n"
                + "Para sair pulsa 's'.";

        // Variables que empregaremos dentro do switch
        Scanner sc = new Scanner(System.in);

        System.out.println("\nServizos listos para ser usados.");
        while (!op.equals("s")) {
            System.out.println(menu);
            op = sc.nextLine();

            switch (op) {
                case "1":
                    System.out.println("\nEmpregando o servicio Calculadora.");
                    calculadora(calculadora);
                    break;
                case "2":
                    System.out.println("\nEmpregando o servicio Analizador de Textos.");
                    analizador(analizador);
                    break;
            }
        }
    }

    private static void calculadora(Calculadora calculadora) {
        Scanner sc = new Scanner(System.in);
        String op = "";
        int a, b;
        String linha;
        String[] nums;
        int[] numeros;

        String menuCalculadora = "1. Suma de dous enteiros\n"
                + "2. Resta de dous enteiros\n"
                + "3. Multiplicacion de dous enteiros\n"
                + "4. Division de dous enteiros\n"
                + "5. Potencia dun enteiro\n"
                + "6. Raiz cadrada dun enteiro\n"
                + "7. Logaritmo neperiano dun enteiro\n"
                + "8. Maximo dunha lista de enteiros\n"
                + "9. Minimo dunha lista de enteiros\n"
                + "10. Media dunha lista de enteiros\n"
                + "11. Mediana dunha lista de enteiros\n"
                + "12. Moda dunha lista de enteiros\n"
                + "13. Desviacion tipica dunha lista de enteiros\n"
                + "Para cambiar de servizo pulsa 'm'.";

        while (!op.equals("m")) {
            System.out.println("\nSelecciona unha operacion: ");
            System.out.println(menuCalculadora);
            op = sc.nextLine();

            switch (op) {
                case "1":
                    System.out.println("Introduce os dous sumandos: ");
                    a = sc.nextInt();
                    b = sc.nextInt();
                    // Limpamos o buffer
                    sc.nextLine();
                    System.out.println("\n--> " + calculadora.suma(a, b));
                    break;
                case "2":
                    System.out.println("Introduce minuendo e o sustraendo: ");
                    a = sc.nextInt();
                    b = sc.nextInt();
                    // Limpamos o buffer
                    sc.nextLine();
                    System.out.println("\n--> " + calculadora.resta(a, b));
                    break;
                case "3":
                    System.out.println("Introduce os dous factores: ");
                    a = sc.nextInt();
                    b = sc.nextInt();
                    sc.nextLine();
                    System.out.println("\n--> " + calculadora.mult(a, b));
                    break;
                case "4":
                    do {
                        System.out.println("Introduce o dividendo e o divisor: ");
                        a = sc.nextInt();
                        b = sc.nextInt();
                        sc.nextLine();
                        if (b == 0) {
                            System.out.println("o dividor debe ser distinto de 0");
                        }
                    } while (b == 0);
                    System.out.println("\n--> " + calculadora.div(a, b));
                    break;
                case "5":
                    System.out.println("Introduce a base e o exponhente: ");
                    a = sc.nextInt();
                    b = sc.nextInt();
                    sc.nextLine();
                    System.out.println("\n--> " + calculadora.pot(a, b));
                    break;
                case "6":
                    do {
                        System.out.println("Introduce o enteiro positivo do cal queres calcular a raiz cadrada: ");
                        a = sc.nextInt();
                        sc.nextLine();
                        if (a < 0) {
                            System.out.println("O enteiro debe ser positivo");
                        }
                    } while (a < 0);
                    System.out.println("\n--> " + calculadora.raiz_cadrada(a));
                    break;
                case "7":
                    do {
                        System.out.println("Introduce o enteiro positivo do cal queres calcular o logaritmo neperiano: ");
                        a = sc.nextInt();
                        sc.nextLine();
                        if (a < 0) {
                            System.out.println("O enteiro debe ser positivo");
                        }
                    } while (a < 0);
                    System.out.println("\n--> " + calculadora.ln(a));
                    break;
                case "8":
                    System.out.println("Introduce unha lista de numeros enteiros: ");
                    linha = sc.nextLine();
                    nums = linha.split(" ");
                    numeros = new int[nums.length];

                    try {
                        for (int i = 0; i < nums.length; i++) {
                            numeros[i] = Integer.parseInt(nums[i]);
                        }
                        System.out.println("\n--> " + calculadora.max(numeros));
                    } catch (NumberFormatException e) {
                        System.out.println("Lista de enteiros non valida");
                    }
                    break;
                case "9":
                    System.out.println("Introduce unha lista de numeros enteiros: ");
                    linha = sc.nextLine();
                    nums = linha.split(" ");
                    numeros = new int[nums.length];

                    try {
                        for (int i = 0; i < nums.length; i++) {
                            numeros[i] = Integer.parseInt(nums[i]);
                        }
                        System.out.println("\n--> " + calculadora.min(numeros));
                    } catch (NumberFormatException e) {
                        System.out.println("Lista de enteiros non valida");
                    }
                    break;
                case "10":
                    System.out.println("Introduce unha lista de numeros enteiros: ");
                    linha = sc.nextLine();
                    nums = linha.split(" ");
                    numeros = new int[nums.length];

                    try {
                        for (int i = 0; i < nums.length; i++) {
                            numeros[i] = Integer.parseInt(nums[i]);
                        }
                        System.out.println("\n--> " + calculadora.media(numeros));
                    } catch (NumberFormatException e) {
                        System.out.println("Lista de enteiros non valida");
                    }
                    break;
                case "11":
                    System.out.println("Introduce unha lista de numeros enteiros: ");
                    linha = sc.nextLine();
                    nums = linha.split(" ");
                    numeros = new int[nums.length];

                    try {
                        for (int i = 0; i < nums.length; i++) {
                            numeros[i] = Integer.parseInt(nums[i]);
                        }
                        System.out.println("\n--> " + calculadora.mediana(numeros));
                    } catch (NumberFormatException e) {
                        System.out.println("Lista de enteiros non valida");
                    }
                    break;
                case "12":
                    System.out.println("Introduce unha lista de numeros enteiros: ");
                    linha = sc.nextLine();
                    nums = linha.split(" ");
                    numeros = new int[nums.length];

                    try {
                        for (int i = 0; i < nums.length; i++) {
                            numeros[i] = Integer.parseInt(nums[i]);
                        }

                        String cadea = "";
                        for (Integer num : calculadora.moda(numeros)) {
                            cadea += num + " ";
                        }
                        System.out.println("\n--> " + cadea);

                    } catch (NumberFormatException e) {
                        System.out.println("Lista de enteiros non valida");
                    }
                    break;
                case "13":
                    System.out.println("Introduce unha lista de numeros enteiros: ");
                    linha = sc.nextLine();
                    nums = linha.split(" ");
                    numeros = new int[nums.length];

                    try {
                        for (int i = 0; i < nums.length; i++) {
                            numeros[i] = Integer.parseInt(nums[i]);
                        }
                        System.out.println("\n--> " + calculadora.desv(numeros));
                    } catch (NumberFormatException e) {
                        System.out.println("Lista de enteiros non valida");
                    }
                    break;
            }

        }
    }

    private static void analizador(Analizador analizador) {
        Scanner sc = new Scanner(System.in);
        String op = "", cadea, palabra;

        String menuAnalizador = "1. Contar palabras\n"
                + "2. Contar caracteres\n"
                + "3. Contar frases\n"
                + "4. Veces que aparece unha palabra\n"
                + "5. Palabra mais usada\n"
                + "6. Palabra menos usada\n"
                + "7. Reemprazar palabra\n"
                + "Para cambiar de servizo pulsa 'm'.";

        while (!op.equals("m")) {
            System.out.println("\nSelecciona unha operacion: ");
            System.out.println(menuAnalizador);
            op = sc.nextLine();

            switch (op) {
                case "1":
                    System.out.println("Introduce unha ou varias frases: ");
                    cadea = sc.nextLine();
                    System.out.println("\n--> " + analizador.contar_palabras(cadea));
                    break;
                case "2":
                    System.out.println("Introduce unha ou varias frases: ");
                    cadea = sc.nextLine();
                    System.out.println("\n--> " + analizador.contar_caracteres(cadea));
                    break;
                case "3":
                    System.out.println("Introduce unha ou varias frases: ");
                    cadea = sc.nextLine();
                    System.out.println("\n--> " + analizador.contar_frases(cadea));
                    break;
                case "4":
                    System.out.println("Introduce unha ou varias frases: ");
                    cadea = sc.nextLine();
                    System.out.println("Introduce a palabra: ");
                    palabra = sc.nextLine();
                    System.out.println("\n--> " + analizador.contar_palabra(cadea, palabra.toLowerCase()));
                    break;
                case "5":
                    System.out.println("Introduce unha ou varias frases: ");
                    cadea = sc.nextLine();
                    System.out.println("\n--> " + analizador.pal_mais_usada(cadea));
                    break;
                case "6":
                    System.out.println("Introduce unha ou varias frases: ");
                    cadea = sc.nextLine();
                    System.out.println("\n--> " + analizador.pal_menos_usada(cadea));
                    break;
                case "7":
                    System.out.println("Introduce unha ou varias frases: ");
                    cadea = sc.nextLine();
                    System.out.println("Introduce a palabra que queres substituir: ");
                    String palabra_antiga = sc.nextLine();
                    System.out.println("Introduce a palabra que queres substituir por " + palabra_antiga + ": ");
                    palabra = sc.nextLine();
                    System.out.println("\n--> " + analizador.subs_palabra(cadea, palabra_antiga, palabra));
                    break;
            }
        }
    }

}
