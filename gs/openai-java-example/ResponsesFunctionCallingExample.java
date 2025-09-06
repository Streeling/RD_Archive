package com.openai.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.core.JsonObject;
import com.openai.core.JsonValue;
import com.openai.models.ChatModel;
import com.openai.models.responses.EasyInputMessage;
import com.openai.models.responses.FunctionTool;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.ResponseFunctionToolCall;
import com.openai.models.responses.ResponseInputItem;
import com.openai.models.responses.ResponseOutputMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.openai.core.ObjectMappers.jsonMapper;

public final class ResponsesFunctionCallingExample {
    private ResponsesFunctionCallingExample() {}

    public static void main(String[] args) {
        // Configures using one of:
        // - The `OPENAI_API_KEY` environment variable
        // - The `OPENAI_BASE_URL` and `AZURE_OPENAI_KEY` environment variables
        OpenAIClient client = OpenAIOkHttpClient.fromEnv();

        List<ResponseInputItem> inputItems = new ArrayList<>();
        inputItems.add(ResponseInputItem.ofEasyInputMessage(EasyInputMessage.builder()
                .role(EasyInputMessage.Role.USER)
                .content("How good are the following SDKs: OpenAI Java SDK, Unknown Company SDK")
                .build()));
        // Use a builder so that we can append more messages to it below.
        // Each time we call .build()` we get an immutable object that's unaffected by future mutations of the builder.
        ResponseCreateParams.Builder createParamsBuilder = ResponseCreateParams.builder()
                .model(ChatModel.GPT_3_5_TURBO)
                .addTool(FunctionTool.builder()
                        .name("get-sdk-quality")
                        .description("Gets the quality of the given SDK.")
                        .strict(false)
                        .parameters(FunctionTool.Parameters.builder()
                                .putAdditionalProperty("type", JsonValue.from("object"))
                                .putAdditionalProperty(
                                        "properties", JsonValue.from(Map.of("name", Map.of("type", "string"))))
                                .putAdditionalProperty("required", JsonValue.from(List.of("name")))
                                .putAdditionalProperty("additionalProperties", JsonValue.from(false))
                                .build())
                        .build())
                .inputOfResponse(inputItems);

        client.responses().create(createParamsBuilder.build()).output()
                // Add each assistant message onto the builder so that we keep track of the conversation for asking a
                // follow-up question later.
                .forEach(item -> {
                    if (item.isMessage()) {
                        ResponseOutputMessage message = item.asMessage();
                        inputItems.add(ResponseInputItem.ofResponseOutputMessage(message));
                        System.out.println(message);
                    } else if (item.isFunctionCall()) {
                        ResponseFunctionToolCall toolCall = item.asFunctionCall();
                        String content = callFunction(item.asFunctionCall());
                        // Add the tool call result to the conversation
                        inputItems.add(ResponseInputItem.ofFunctionCall(toolCall));
                        inputItems.add(ResponseInputItem.ofFunctionCallOutput(ResponseInputItem.FunctionCallOutput.builder()
                                .callId(toolCall.callId())
                                .output(content)
                                .build()));
                        System.out.println(content);
                    }
                });
        System.out.println();

        // Ask a follow-up question about the function call result.
        createParamsBuilder.inputOfResponse(inputItems);
        client.responses().create(createParamsBuilder.build()).output().stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .forEach(outputText -> System.out.println(outputText.text()));
    }

    private static String callFunction(ResponseFunctionToolCall function) {
        if (!function.name().equals("get-sdk-quality")) {
            throw new IllegalArgumentException("Unknown function: " + function.name());
        }

        JsonValue arguments;
        try {
            arguments = JsonValue.from(jsonMapper().readTree(function.arguments()));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Bad function arguments", e);
        }

        String sdkName = ((JsonObject) arguments).values().get("name").asStringOrThrow();
        if (sdkName.contains("OpenAI")) {
            return sdkName + ": It's robust and polished!";
        }

        return sdkName + ": *shrug*";
    }
}
