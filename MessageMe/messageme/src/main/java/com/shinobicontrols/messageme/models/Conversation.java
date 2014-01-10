package com.shinobicontrols.messageme.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sdavies on 09/01/2014.
 */
public class Conversation {

    private String sender;
    private List<Message> messages;

    public Conversation(String sender) {
        this.sender = sender;
        this.messages = new ArrayList<Message>();
    }

    public Conversation(String sender, List<Message> messages) {
        this.sender = sender;
        this.messages = messages;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public String getSender() {
        return sender;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    @Override
    public String toString() {
        return sender;
    }
}
