package com.garnet.errorhandling.exception.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garnet.errorhandling.openai.OpenAIClient;
import com.garnet.errorhandling.openai.model.ChatCompletionRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenAIErrorInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(OpenAIErrorInterceptor.class);

    private final OpenAIClient openAIClient;
    private final String model;

    @Autowired
    public OpenAIErrorInterceptor(OpenAIClient openAIClient, @Value("${openai.model:gpt-3.5-turbo}") String model) {
        this.openAIClient = openAIClient;
        this.model = model;
    }

    public static String prettifyJson(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Object jsonObject = objectMapper.readValue(json, Object.class);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (Exception e) {
            return json;
        }
    }

    public void handle(Exception ex) {
        CompletableFuture.runAsync(() -> enrichErrorResponse(ex));
    }

    private void enrichErrorResponse(Exception ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));

        ChatCompletionRequest request = new ChatCompletionRequest(model,
            "Providing the following StackTrace, "
                + "I would like you to return an explanation, possible_solution, criticality_level, and user_friendly_message in JSON format. "
                + sw);

        openAIClient.chatCompletion(request)
            .stream()
            .findFirst()
            .ifPresent(choice -> logger.error(prettifyJson(choice.getMessage().getContent())));
    }
}
