package org.mql.generative.ai.rag.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;

@Configuration
public class ChromaStoreConfig {
	 @Value("${chroma.url}")
	    private String chromaUrl;

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
      return ChromaEmbeddingStore.builder()
                .baseUrl(chromaUrl)
                .build();        
                
    }
}
