package com.example.ProducerMicroservice.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
@EnableJms
public class ActiveMQConfig {

    @Value("${activemq.broker.url}")
    private String BROKER_URL;

    @Value("${activemq.broker.user}")
    private String BROKER_USERNAME;

    @Value("${activemq.broker.password}")
    private String BROKER_PASSWORD;

    @Value("${activemq.queue.priority}")
    public String priorityQueue;

    @Value("${activemq.queue.non-priority}")
    public String nonPriorityQueue;

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(BROKER_URL);
        factory.setUserName(BROKER_USERNAME);
        factory.setPassword(BROKER_PASSWORD);
        factory.setTrustAllPackages(true); // required for POJO serialization
        return factory;
    }

    @Bean
    public MappingJackson2MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT); // send POJO as JSON text
        converter.setTypeIdPropertyName("_type");   // include class info for deserialization
        return converter;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
        jmsTemplate.setDeliveryPersistent(true); // ensures messages are durable
        return jmsTemplate;
    }
}
