package com.chat.messaging;

import com.chat.WebSocketConfig;
import com.chat.common.MessageHolder;
import com.chat.helper.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * Created by amit on 26/12/16.
 */

public class Consumer implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private Converter converter;

    @Override
    public void onMessage(Message message) {

        try {
            MessageHolder messageHolder = converter.convertToObject(message.getBody());
            messagingTemplate.convertAndSend(WebSocketConfig.SIMPLE_BROKER+"/"+messageHolder.getDestinationClientId(),
                    messageHolder);
        } catch (Exception e) {
            LOGGER.error("Error while broadcasting message,{},{}",e.getMessage());
        }
    }
}
