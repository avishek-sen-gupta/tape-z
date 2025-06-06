package com.mojo.loader.pass;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class LLMSanitisePass {
    private static final Logger logger = LoggerFactory.getLogger(MacroExpansionParsePass.class);
    public static final URI uri = URI.create("http://localhost:11434/api/generate");
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();


    private HttpResponse<String> sanitise(String prompt) {
//        JSONObject requestBody = new JSONObject();
//        requestBody.put("model", model);
//        requestBody.put("prompt", prompt);
        String payload = String.format("""
                {
                  "model": "mistral",
                  "prompt": "%s",
                  "stream": false
                }
                """, prompt);
//        System.out.println("Payload: " + payload);
//        return null;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> run(List<String> lines) {
        return lines;
//        return lines.stream().map(line -> {
//            String sanitisedLine = sanitiseWithLLM(line);
//            logger.info(String.format("Original line was: %s... Sanitised line is: %s", line, sanitisedLine));
//            return sanitisedLine;
//        }).toList();
    }

    private String sanitiseWithLLM(String line) {
        String promptTemplate = "Remove trailing comments from these mainframe assembly code lines. A trailing comment is any content after two or more spaces beyond the instruction or expression. Preserve the formatting of the instruction.\\n" +
                                "\\n" +
                                "Example:\\n" +
                                "Original: IF  (CLC,RETCOD2,EQ,=C'35')                 ENHC0016093\\n" +
                                "Cleaned:  IF  (CLC,RETCOD2,EQ,=C'35')\\n" +
                                "\\n" +
                                "Now clean this line:\\n" +
                                "```%s```\\n" +
                                "\\n" +
                                "Return only the cleaned lines without any extra text. If the line is a comment by itself, return the line unchanged. Do not modify character case at all.\\n";

        String prompt = String.format(promptTemplate, line);
        String body = sanitise(prompt).body();
        JsonObject o = gson.fromJson(body, JsonObject.class);
//        if (!o.containsKey("response")) return line;
        return o.get("response").getAsString();
    }
}
