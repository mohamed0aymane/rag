package org.mql.generative.ai.rag.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.model.openai.OpenAiChatModel;

@Configuration
public class OpenAiConfig {
	
        @Value("${openai.url}")
        private String openAiApiUrl;
        @Value("${openai.key}")
        private String openAiApiKey; 
        @Value("${openai.chat}")  
        private String openAiChatModel;
        @Value("${model.timeout}")   
        private String timeout;  

        @Bean
        public OpenAiChatModel chatModel() {
                return OpenAiChatModel.builder()
                                .baseUrl(openAiApiUrl)
                                .apiKey(openAiApiKey)
                                .modelName(openAiChatModel)
                                .timeout(Duration.ofMinutes(Integer.valueOf(timeout)))
                                .build();
        }
}
