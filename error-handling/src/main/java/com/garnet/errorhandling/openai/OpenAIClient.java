package com.garnet.errorhandling.openai;

import static org.springframework.util.CollectionUtils.isEmpty;

import com.garnet.errorhandling.openai.model.ChatCompletionRequest;
import com.garnet.errorhandling.openai.model.ChatCompletionResponse;
import com.garnet.errorhandling.openai.model.ChatCompletionResponse.Choice;
import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OpenAIClient {

    private static final Logger logger = LoggerFactory.getLogger(OpenAIClient.class);

    @Value("${openai.api_key}")
    private String apiKey;

    @Value("${openai.chat_completions.api_url}")
    private String apiUrl;

    private RestTemplate restTemplate;

    @PostConstruct
    public void postConstruct() {
        this.restTemplate = createRestTemplate();
    }

    public List<Choice> chatCompletion(ChatCompletionRequest request) {
        try {
            ChatCompletionResponse response = restTemplate.postForObject(apiUrl, request, ChatCompletionResponse.class);

            if (response == null || isEmpty(response.getChoices())) {
                return Collections.emptyList();
            }
            return response.getChoices();

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return Collections.emptyList();
    }

    private RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(createBearerTokenInterceptor()));
        return restTemplate;
    }

    private ClientHttpRequestInterceptor createBearerTokenInterceptor() {
        return (request, body, execution) -> {
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);
            return execution.execute(request, body);
        };
    }
}
