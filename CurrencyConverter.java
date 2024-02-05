import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CurrencyConverter {

    public static void main(String[] args) {
        try {
            // Allow the user to choose the base and target currencies
            String baseCurrency = getUserInput("Enter the base currency code: ");
            String targetCurrency = getUserInput("Enter the target currency code: ");

            // Fetch real-time exchange rates from the API
            double exchangeRate = getExchangeRate(baseCurrency, targetCurrency);

            // Take input from the user for the amount they want to convert
            double amount = Double.parseDouble(getUserInput("Enter the amount to convert: "));

            // Convert the input amount to the target currency using the exchange rate
            double convertedAmount = amount * exchangeRate;

            // Display the result
            System.out.printf("%.2f %s is equal to %.2f %s%n", amount, baseCurrency, convertedAmount, targetCurrency);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getUserInput(String message) throws IOException {
        System.out.print(message);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    private static double getExchangeRate(String baseCurrency, String targetCurrency) throws IOException {
        String apiUrl = "https://api.exchangerate-api.com/v4/latest/" + baseCurrency;
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Parse JSON response to get the exchange rate
            String jsonResponse = response.toString();
            String rateKey = "\"" + targetCurrency + "\":";

            int rateIndex = jsonResponse.indexOf(rateKey);
            int startIndex = jsonResponse.indexOf(":", rateIndex) + 1;
            int endIndex = jsonResponse.indexOf(",", startIndex);

            return Double.parseDouble(jsonResponse.substring(startIndex, endIndex).trim());
        } finally {
            connection.disconnect();
        }
    }
}
