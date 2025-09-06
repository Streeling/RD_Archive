package com.openai.example;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.core.JsonValue;
import com.openai.models.ChatModel;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.ResponseFormatTextJsonSchemaConfig;
import com.openai.models.responses.ResponseTextConfig;

import java.util.List;
import java.util.Map;

public final class ResponsesStructuredOutputsExample {
    private ResponsesStructuredOutputsExample() {}

    public static void main(String[] args) {
        // Configures using one of:
        // - The `OPENAI_API_KEY` environment variable
        // - The `OPENAI_BASE_URL` and `AZURE_OPENAI_KEY` environment variables
        OpenAIClient client = OpenAIOkHttpClient.fromEnv();

        // TODO: Update this once we support extracting JSON schemas from Java classes
        ResponseFormatTextJsonSchemaConfig.Schema schema = ResponseFormatTextJsonSchemaConfig.Schema.builder()
                .putAdditionalProperty("type", JsonValue.from("object"))
                .putAdditionalProperty(
                        "properties", JsonValue.from(Map.of("employees", Map.of("items", Map.of("type", "string")))))
                .build();
        ResponseCreateParams createParams = ResponseCreateParams.builder()
                .model(ChatModel.GPT_4O_MINI)
                .maxOutputTokens(2048)
                .text(ResponseTextConfig.builder()
                        .format(ResponseFormatTextJsonSchemaConfig.builder()
                                .name("employee-list")
                                .schema(schema)
                                .build()
                        )
                        .build())
                .input("Who works at OpenAI?")
                .build();

        client.responses().create(createParams).output().stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .forEach(outputText -> System.out.println(outputText.text()));
    }
}
