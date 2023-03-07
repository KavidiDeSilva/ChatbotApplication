import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import java.net.MalformedURLException;
import java.io.IOException;

public class ChatbotApplication {

    private static final String CHATGPT_API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String CHATGPT_API_KEY = "CHATGPT_API_KEY";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String userInput = "";

        while (!userInput.equals("quit")) {
            System.out.print("You: ");
            userInput = scanner.nextLine();

            if (!userInput.equals("quit")) {
                String chatbotResponse = getChatbotResponse(userInput);
                System.out.println("Chatbot: " + chatbotResponse);
            }
        }
    }

    private static String getChatbotResponse(String userInput) {
    try {
        URL url = new URL(CHATGPT_API_ENDPOINT);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + CHATGPT_API_KEY);

        String data = String.format("{\"prompt\": \"%s\", \"max_tokens\": 100, \"temperature\": 0.8}", userInput);

        byte[] postData = data.getBytes("UTF-8");

        connection.setDoOutput(true);
        connection.getOutputStream().write(postData);

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        StringBuilder builder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        reader.close();
        connection.disconnect();

        String response = builder.toString();
        int startIndex = response.indexOf("\"text\": \"") + 9;
        int endIndex = response.indexOf("\",\"", startIndex);

        return response.substring(startIndex, endIndex);
    } catch (MalformedURLException e) {
        return "Sorry, the URL for the OpenAI API is not valid.";
    } catch (IOException e) {
        return "Sorry, there was a problem connecting to the OpenAI API.";
    } catch (Exception e) {
        e.printStackTrace();
        return "Sorry, there was an unexpected error while processing your request.";
    }
}

}
