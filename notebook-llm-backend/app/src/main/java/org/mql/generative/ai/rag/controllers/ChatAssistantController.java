package org.mql.generative.ai.rag.controllers;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import org.mql.generative.ai.rag.services.ChatAssistantService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/notebook")
public class ChatAssistantController {

    private final ChatAssistantService chatAssistantService;

    public ChatAssistantController(ChatAssistantService chatAssistantService) {
        this.chatAssistantService = chatAssistantService;
    }

    @PostMapping("/ask")
    public Map<String, String> chat(@RequestBody Map<String, String> body) {
        String prompt = body.get("question");

        if (prompt == null || prompt.isEmpty())
        	return Map.of("response", "Question is empty!");
        Instant now = Instant.now();
        String result = chatAssistantService.askQuestion(prompt);
        double timeProcess = Duration.between(now, Instant.now()).toMillis();

        return Map.of("response", result);
    }
}
