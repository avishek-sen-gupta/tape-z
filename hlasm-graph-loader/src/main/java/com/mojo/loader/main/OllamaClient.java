package com.mojo.loader.main;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OllamaClient {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String response = sanitise("What is the capital of India?", client);
        System.out.println("Response:");
        System.out.println(response);
    }

    private static String sanitise(String prompt, HttpClient client) throws IOException, InterruptedException {
        String payload = String.format("""
                {
                  "model": "mistral",
                  "prompt": "%s",
                  "stream": false
                }
                """, prompt);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:11434/api/generate"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
        return jsonObject.get("response").getAsString();
    }
}
