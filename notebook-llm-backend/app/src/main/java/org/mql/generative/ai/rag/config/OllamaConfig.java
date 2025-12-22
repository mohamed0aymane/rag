package org.mql.generative.ai.rag.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


import dev.langchain4j.model.ollama.OllamaEmbeddingModel;

@Configuration
public class OllamaConfig {
	
	   @Value("${ollama.embedding}")
	    private String embeddingModel;
	   
	    @Value("${ollama.url}")
	    private String ollamaUrl;
	    
	    @Value("${model.timeout}")
	    private String timeout;

    
    @Bean
    @Primary
    OllamaEmbeddingModel ollamaEmbeddingModel() {
        return OllamaEmbeddingModel.builder()
                .baseUrl(ollamaUrl)
                .modelName(embeddingModel)
                .timeout(Duration.ofMinutes(Integer.valueOf(timeout)))
                .build();
    }

  
}
