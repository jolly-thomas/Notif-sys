package com.example.ConsumerMicroservice.config;

import com.example.ConsumerMicroservice.model.NotificationMessage;
import jakarta.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.broker.region.policy.RedeliveryPolicyMap;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJms
public class ActiveMQConfig {

    @Value("${activemq.broker.url}")
    private String brokerUrl;

    @Value("${activemq.broker.user}")
    private String username;

    @Value("${activemq.broker.password}")
    private String password;

    @Value("${activemq.queue.priority}")
    private String priority;

    @Value("${activemq.queue.non-priority}")
    private String nonPriority;

    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(username, password, brokerUrl);
        factory.setTrustAllPackages(true);

//        // Priority queue policy
//        RedeliveryPolicy priorityPolicy = new RedeliveryPolicy();
//        priorityPolicy.setMaximumRedeliveries(3);
//        priorityPolicy.setInitialRedeliveryDelay(1000);
//        priorityPolicy.setUseExponentialBackOff(true);
//        priorityPolicy.setBackOffMultiplier(2);

        // Non-priority queue policy
        RedeliveryPolicy nonPriorityPolicy = new RedeliveryPolicy();
        nonPriorityPolicy.setMaximumRedeliveries(3);
        nonPriorityPolicy.setInitialRedeliveryDelay(2000);
        nonPriorityPolicy.setUseExponentialBackOff(true);
        nonPriorityPolicy.setBackOffMultiplier(2);

        RedeliveryPolicyMap policyMap = new RedeliveryPolicyMap();
//        policyMap.put(new ActiveMQQueue(priority), priorityPolicy);
        policyMap.put(new ActiveMQQueue(nonPriority), nonPriorityPolicy);

        return factory;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setMessageConverter(jacksonJmsMessageConverter());
        factory.setConcurrency("1-1"); // single consumer
        factory.setSessionTransacted(true);
        return factory;
    }

    @Bean
    public MappingJackson2MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");

        Map<String, Class<?>> typeIdMappings = new HashMap<>();
        typeIdMappings.put("NotificationMessage", NotificationMessage.class);
        converter.setTypeIdMappings(typeIdMappings);

        return converter;
    }
}
