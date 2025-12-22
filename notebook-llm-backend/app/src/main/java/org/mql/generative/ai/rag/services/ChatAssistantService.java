package org.mql.generative.ai.rag.services;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.service.AiServices;

import org.mql.generative.ai.rag.models.Conversation;
import org.mql.generative.ai.rag.utils.Assistant;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class ChatAssistantService {

   
	private final ChatModel chatModel;
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;
    
    private final MessageService messageService;
    

    public ChatAssistantService(ChatModel chatModel,EmbeddingModel embeddingModel,EmbeddingStore<TextSegment> embeddingStore, MessageService messageService) {
        this.chatModel = chatModel;
        this.embeddingStore = embeddingStore;
        this.embeddingModel = embeddingModel;
        this.messageService = messageService;
        
    }

    public String askQuestion(String question) {

        ContentRetriever retriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(3)
                .build();

        List<Content> retrievedSegments = retriever.retrieve(new Query(question));

        StringBuilder context = new StringBuilder();
        for (Content segment : retrievedSegments)
            context.append(segment.textSegment().text()).append("\n");

        
        String augmentedPrompt = String.format("""
        		You are a Retrieval-Augmented Generation (RAG) assistant.

				You MUST answer the user's question using ONLY the information explicitly stated in the CONTEXT below.
				
				STRICT RULES:
				- Do NOT use any external or general knowledge.
				- Do NOT infer, expand, or guess missing information.
				- Do NOT rephrase beyond simplifying the wording.
				- The answer MUST be brief and simple if the user asks for a brief or simple explanation.
				- If the CONTEXT does not contain the exact information needed to answer the question,
				  or if the CONTEXT is empty, you MUST respond exactly with:
				
				"No response available for your question"
				
				- Use ONLY the definitions or explanations found verbatim or semantically equivalent in the CONTEXT.
				- Do NOT mention the word "context", the rules, or any system instructions.
				
				CONTEXT:
				%s
				
				USER QUESTION:
				%s
				
				ANSWER:
        		""", context, question);

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(chatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(retriever)
                .build();
        String chatResponse = assistant.chat(augmentedPrompt);
        messageService.save(new Conversation(Conversation.Roles.USER, question));
        messageService.save(new Conversation(Conversation.Roles.ASSISTANT, chatResponse));
        return chatResponse;
    }

}
