package com.chat;

import com.chat.messaging.Consumer;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by amit on 26/12/16.
 */
@Configuration
@ComponentScan(basePackages = "com.chat.*")
public class RabbitConfig {

    public final static String QUEUE_NAME = "chat.message";
    private final static String HOST_NAME = "localhost";

    @Autowired
    private Queue queue;

    @Bean
    public Consumer consumer(){
        return new Consumer();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(QUEUE_NAME);
        return template;
    }

    @Bean
    public AmqpAdmin amqpAdmin(){
        AmqpAdmin admin = new RabbitAdmin(connectionFactory());
        admin.declareQueue(queue);
        return admin;
    }

    @Bean
    public Queue queue(){
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public ConnectionFactory connectionFactory(){
        ConnectionFactory connectionFactory = new CachingConnectionFactory(HOST_NAME);
        return connectionFactory;
    }
    
    @Bean
    SimpleMessageListenerContainer container() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueues(queue());
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setMessageListener(consumer());
        return container;
    }

}
