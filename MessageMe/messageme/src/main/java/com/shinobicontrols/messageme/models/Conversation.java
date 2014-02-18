/*
Copyright 2014 Scott Logic Ltd

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
*/

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
