package com.chat.controller;

import com.chat.common.MessageHolder;
import com.chat.messaging.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Created by amit on 26/12/16.
 */
@Controller
public class ChatController {

    @Autowired
    private Producer producer;

    @MessageMapping(value = "/message")
    public void sendMessage(MessageHolder messageHolder){
        producer.addMessage(messageHolder);
    }
}
