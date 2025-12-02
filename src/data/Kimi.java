package data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class Kimi {

    private static final String API_KEY;
    public static final String KIMI_K2_0905_PREVIEW = "kimi-k2-0905-preview";
    public static final String KIMI_K2_0711_PREVIEW = "kimi-k2-0711-preview";
    public static final String KIMI_K2_THINKING = "kimi-k2-thinking";
    public static final String KIMI_LATEST = "kimi-latest";

    static {
        String apiKey = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\.apiKey")))) {
            apiKey = reader.readLine();
        } catch (IOException e) {
            System.out.println("API_KEY 读取错误!");
            System.exit(0);
        }
        API_KEY = apiKey;
    }

    private static final Kimi kimi = new Kimi();

    public static Kimi getKimi() {
        return kimi;
    }

    public String request(String model, Message[] messages, double temperature) {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.moonshot.cn/v1/chat/completions"))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        getRequestBody(model, messages, Math.min(Math.max(0,temperature),1))
                ))
                .build();
        HttpResponse<String> resp;
        try {
            resp = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return resp.body();
    }

    private String getRequestBody(String model, Message[] messages, double temperature) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"model\":\"").append(model).append("\",\"messages\":[");
        for (int i = 0; i < messages.length; i++) {
            sb.append("{\"role\":\"").append(messages[i].role()).append("\",\"content\":\"").append(messages[i].content()).append("\"}");
            if (i < messages.length - 1) sb.append(",");
        }
        sb.append("],\"temperature\":").append(temperature).append("}");
        return sb.toString();
    }
}
