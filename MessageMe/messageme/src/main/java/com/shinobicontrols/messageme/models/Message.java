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

import java.util.Date;

/**
 * Created by sdavies on 09/01/2014.
 */
public class Message {

    private String Content;
    private String Sender;
    private String Recipient;
    private Date Time;

    public Message(String content, String sender, String recipient, Date time) {
        Content = content;
        Sender = sender;
        Recipient = recipient;
        Time = time;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getRecipient() {
        return Recipient;
    }

    public void setRecipient(String recipient) {
        Recipient = recipient;
    }

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }

    @Override
    public String toString() {
        return getContent() + "  -  " + getTime().toString();
    }
}
