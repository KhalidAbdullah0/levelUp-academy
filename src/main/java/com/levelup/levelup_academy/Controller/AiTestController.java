package com.levelup.levelup_academy.Controller;

import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiTestController {

    private final OpenAIClient openAIClient;

    @GetMapping("/test")
    public String test() {
        ChatCompletionCreateParams params =
                ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_4_1_MINI) // gpt-4.1-mini, fast & cheap for code/tasks :contentReference[oaicite:4]{index=4}
                        .addUserMessage("Say: \"Hello Abdullah from OpenAI via Spring Boot\"")
                        .build();

        ChatCompletion completion =
                openAIClient.chat().completions().create(params);

        // Get the first message text
        return completion.choices()
                .get(0)
                .message()
                .content()
                .get()
                .trim();
    }
}