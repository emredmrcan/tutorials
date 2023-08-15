package com.garnet.errorhandling.openai.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ChatCompletionRequest {

    private String model;
    private List<Message> messages = new ArrayList<>(1);
    private int max_tokens = 150;
    private double temperature = 0.2;

    public ChatCompletionRequest(String model, String prompt) {
        this.model = model;
        this.messages.add(new Message("system", prompt));
    }
}

