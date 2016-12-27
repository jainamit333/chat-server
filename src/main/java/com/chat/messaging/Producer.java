package com.chat.messaging;

import com.chat.common.MessageHolder;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by amit on 26/12/16.
 */
@Service
public class Producer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //TODO should be hazelcast shared object when app running on distributed application
    //TODO It should contain the distributed lock on value.
    private Table<String,String,MessageHolder> lastMessage = HashBasedTable.create();

    public void addMessage(MessageHolder messageHolder){
        LOGGER.debug("Message for Rabbit , {}",messageHolder);
        if(notDuplicate(messageHolder)){
            rabbitTemplate.convertAndSend(messageHolder);
        }
    }

    private boolean notDuplicate(MessageHolder messageHolder) {

        try{
            MessageHolder previousMessage = lastMessage.put(messageHolder.getSourceClientId(),
                    messageHolder.getDestinationClientId(),messageHolder);
            return !messageHolder.equals(previousMessage);
        }catch (Exception e){
            LOGGER.error("Error while checking for duplicate message");
            return false;
        }
    }
}
