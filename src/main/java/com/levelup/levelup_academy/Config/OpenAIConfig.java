package com.levelup.levelup_academy.Config;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {

    @Bean
    public OpenAIClient openAIClient() {
        // Reads OPENAI_API_KEY (and optional ORG/PROJECT) from environment
        return OpenAIOkHttpClient.fromEnv();
    }
}
