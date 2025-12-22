package org.mql.generative.ai.rag.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chat_messages")
public class Conversation {

    @Id
    private String id;

    private Roles roles;
    private String text;

    public enum Roles {
        USER, ASSISTANT
    }

    public Conversation() {}

    public Conversation(Roles roles, String text) {
        this.roles= roles;
        this.text = text;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

  
    public Roles getRoles() {
		return roles;
	}

	public void setRoles(Roles roles) {
		this.roles = roles;
	}

	public String getText() { 
		return text; 
		}
    public void setText(String text) {
    	this.text = text; 
    	}
}
