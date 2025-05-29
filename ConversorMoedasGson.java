import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ConversorMoedasGson {

    static final String API_KEY = "APY-KEY";
    static final String BASE_URL = " https://v6.exchangerate-api.com/v6/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n=== CONVERSOR DE MOEDAS ===");
            System.out.println("1 - Dólar (USD) -> Peso Argentino (ARS)");
            System.out.println("2 - Peso Argentino (ARS) -> Dólar (USD)");
            System.out.println("3 - Dólar (USD) -> Real Brasileiro (BRL)");
            System.out.println("4 - Real Brasileiro (BRL) -> Dólar (USD)");
            System.out.println("5 - Dólar (USD) -> Peso Colombiano (COP)");
            System.out.println("6 - Peso Colombiano (COP) -> Dólar (USD)");
            System.out.println("7 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();

            if (opcao >= 1 && opcao <= 6) {
                System.out.print("Digite o valor a ser convertido: ");
                double valor = scanner.nextDouble();

                String de = "";
                String para = "";

                switch (opcao) {
                    case 1: de = "USD"; para = "ARS"; break;
                    case 2: de = "ARS"; para = "USD"; break;
                    case 3: de = "USD"; para = "BRL"; break;
                    case 4: de = "BRL"; para = "USD"; break;
                    case 5: de = "USD"; para = "COP"; break;
                    case 6: de = "COP"; para = "USD"; break;
                }

                double taxa = obterTaxaCambio(de, para);
                if (taxa > 0) {
                    double convertido = valor * taxa;
                    System.out.printf("Valor convertido: %.2f %s%n", convertido, para);
                } else {
                    System.out.println("Erro ao obter a taxa de câmbio.");
                }
            } else if (opcao != 7) {
                System.out.println("Opção inválida. Tente novamente.");
            }

        } while (opcao != 7);

        System.out.println("Programa encerrado.");
        scanner.close();
    }

    public static double obterTaxaCambio(String de, String para) {
        try {
            String urlStr = BASE_URL + API_KEY + "/pair/" + de + "/" + para;
            URL url = new URL(urlStr);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(in, JsonObject.class);
            in.close();

            if (json.get("result").getAsString().equals("success")) {
                return json.get("conversion_rate").getAsDouble();
            } else {
                System.out.println("Erro na resposta da API: " + json.toString());
            }

        } catch (Exception e) {
            System.out.println("Erro ao acessar a API: " + e.getMessage());
        }

        return -1;
    }
}
