package com.garnet.errorhandling.openai.model;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatCompletionResponse {

    private List<Choice> choices;

    @Data
    @NoArgsConstructor
    public static class Choice {

        private int index;
        private Message message;
    }
}

