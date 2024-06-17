import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Principal {

    public static void main(String[] args) throws IOException, InterruptedException {
        ConsultaMoneda consulta = new ConsultaMoneda();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting()
                .create();

        ConversionHistory history = new ConversionHistory();
        final String ANSI_RED = "\u001B[31m";
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_BLUE = "\u001B[34m";
        String menu = ANSI_BLUE+ """
                \n**********   MENU PRINCIPAL   **********
                
                \t1) Dólar --->> Quetzal Guatemalteco
                \t2) Quetzal Guatemalteco --->> Dólar
                \t3) Dólar --->> Real brasileño
                \t4) Real brasileño ---> Dólar
                \t5) Dólar --->> Peso Colombiano
                \t6) Peso Colombiano --->> Dólar
                \t7) Dólar ---> Peso Mexicano
                \t8) Peso Mexicano --->>  Dólar
                \t9) Ver historial de conversiones
                \t0) Salir
                
                Escribe la opcion a realizar 
                ****************************************\n
                """+ ANSI_RESET;

        System.out.println("****************************************");
        System.out.println("\tBienvenido al Conversor de Monedas");
        System.out.println("****************************************");
        Scanner opcionMenu = new Scanner(System.in);

        while (true) {
            System.out.println(menu);
            int op = 0;

            try {
                op = opcionMenu.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Opción no válida. Por favor, ingrese un número.");
                opcionMenu.next(); // Limpiar el buffer del escáner
                continue;
            }

            if (op == 0) {
                System.out.println("Finalizando el programa. Muchas gracias por usar nuestros servicios de conversión.");
                break;
            }

            if (op == 9) {
                history.printHistory();
                continue;
            }

            if (op < 0 || op > 9) {
                System.out.println("Opción no válida\n");
                System.out.println("******************************************\n");
                continue;
            }

            double cantidad = 0;
            System.out.println("Ingresa una cantidad que deseas convertir: ");
            try {
                cantidad = opcionMenu.nextDouble();
                if (cantidad > 9999999) {
                    System.out.println("Cantidad demasiado grande. Ingrese una cantridad de máximo 7 dígitos.");
                    continue;
                }
            } catch (InputMismatchException e) {
                System.out.println("Valor no válida. Por favor, ingrese un número entero .");
                opcionMenu.next();
                continue;
            }

            String base = "";
            String target = "";

            switch (op) {
                case 1:
                    base = "USD";
                    target = "GTQ";
                    break;
                case 2:
                    base = "GTQ";
                    target = "USD";
                    break;
                case 3:
                    base = "USD";
                    target = "BRL";
                    break;
                case 4:
                    base = "BRL";
                    target = "USD";
                    break;
                case 5:
                    base = "USD";
                    target = "COP";
                    break;
                case 6:
                    base = "COP";
                    target = "USD";
                    break;
                case 7:
                    base = "USD";
                    target = "MXN";
                    break;
                case 8:
                    base = "MXN";
                    target = "USD";
                    break;
            }

            String direccion = "https://v6.exchangerate-api.com/v6/5920b1ae3f42e675c5999f7c/pair/"
                    + base + "/" + target + "/" + cantidad;
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(direccion))
                        .build();
                HttpResponse<String> response = client
                        .send(request, HttpResponse.BodyHandlers.ofString());

                String json = response.body();

                // Verifica si la respuesta es un objeto JSON
                if (json.trim().startsWith("{")) {
                    Respuesta respuesta = gson.fromJson(json, Respuesta.class);
                    double conversionResult = respuesta.getConversionResult();

                    // Redondear el resultado a dos decimales
                    BigDecimal roundedResult = new BigDecimal(conversionResult).setScale(2, RoundingMode.HALF_UP);
                    System.out.println( ANSI_RED+ "$" + cantidad + " " + base + " equivalen a $" + roundedResult + " " + target+ANSI_RESET);

                    // Agregar el registro al historial
                    history.addRecord(cantidad, base, target, roundedResult.doubleValue());

                    // Guardar historial en un archivo
                    history.saveHistoryToFile("conversion_history.txt");
                } else {
                    System.out.println("Respuesta inesperada: ");
                }

            } catch (JsonSyntaxException e) {
                System.out.println("Error de sintaxis JSON: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Solo números");
            } catch (IOException | InterruptedException e) {
                System.out.println("Fallò la conexión. Por favor, intente de nuevo más tarde.");
            }
        }
    }
}
