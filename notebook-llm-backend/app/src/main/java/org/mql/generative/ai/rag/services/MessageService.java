package org.mql.generative.ai.rag.services;

import org.mql.generative.ai.rag.models.Conversation;
import org.mql.generative.ai.rag.repositories.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository repository;

    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    public Conversation save(Conversation message) {
        return repository.save(message);
    }

    public List<Conversation> findAll() {
        return repository.findAll();
    }
}
