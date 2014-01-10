package com.shinobicontrols.messageme.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sdavies on 09/01/2014.
 */
public class DataProvider {
    private static DataProvider ourInstance = new DataProvider();

    public static DataProvider getInstance() {
        return ourInstance;
    }

    private Map<String, Conversation> conversations;

    private DataProvider() {
        // Create the store
        conversations = new HashMap<String, Conversation>();

        // Create some sample data
        addMessage(new Message("content1", "sender1", "recipient", new Date()));
        addMessage(new Message("content2", "sender1", "recipient", new Date()));
        addMessage(new Message("content3", "sender2", "recipient", new Date()));
        addMessage(new Message("content4", "sender3", "recipient", new Date()));
        addMessage(new Message("content5", "sender4", "recipient", new Date()));
        addMessage(new Message("content6", "sender4", "recipient", new Date()));
        addMessage(new Message("content7", "sender4", "recipient", new Date()));
        addMessage(new Message("content8", "sender4", "recipient", new Date()));
        addMessage(new Message("content9", "sender4", "recipient", new Date()));
    }

    public void addMessage(Message message) {
        if(conversations.containsKey(message.getSender())) {
            // Can add the message to an existing conversation
            conversations.get(message.getSender()).addMessage(message);
        } else {
            // Need to create a new conversation
            Conversation conversation = new Conversation(message.getSender());
            conversation.addMessage(message);
            conversations.put(message.getSender(), conversation);
        }
    }

    public ArrayList<Conversation> getConversations() {
        return new ArrayList<Conversation>(conversations.values());
    }

    public Map<String, Conversation> getConversationMap() {
        return conversations;
    }
}
