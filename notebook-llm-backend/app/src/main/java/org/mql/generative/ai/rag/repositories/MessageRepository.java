package org.mql.generative.ai.rag.repositories;

import org.mql.generative.ai.rag.models.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Conversation, String> {
}
