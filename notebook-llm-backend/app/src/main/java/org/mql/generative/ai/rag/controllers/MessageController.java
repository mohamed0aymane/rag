package org.mql.generative.ai.rag.controllers;

import org.mql.generative.ai.rag.models.Conversation;
import org.mql.generative.ai.rag.services.MessageService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/notebook")
public class MessageController {

    private final MessageService service;

    public MessageController(MessageService service) {
        this.service = service;
    }

    @GetMapping("/conversations")
    public List<Conversation> readAll() {
        return service.findAll();
    }
}
