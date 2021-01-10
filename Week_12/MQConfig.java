package com.sl.homework.week12.activemq.config;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;

import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.jms.Topic;

@Configuration
public class MQConfig {

    // point-to-point，一对一
    // 默认持久化
    @Bean
    public Queue queue() {
        return new ActiveMQQueue("my.queue");
    }

    // publish-subscribe,一个publish可以有多个subscribe
    // 广播，默认不持久化
    @Bean
    public Topic topic() {
        return new ActiveMQTopic("my.topic");
    }

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        String url = ActiveMQConnection.DEFAULT_BROKER_URL;
        return new ActiveMQConnectionFactory("admin", "admin", url);
    }


    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerQueue(ActiveMQConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//        factory.setSessionAcknowledgeMode(JMSContext.AUTO_ACKNOWLEDGE);
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerTopic(ActiveMQConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        // 是否持久化，true为开启持久化
        factory.setPubSubDomain(true);
//        factory.setSessionAcknowledgeMode(JMSContext.AUTO_ACKNOWLEDGE);
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean
    public JmsMessagingTemplate jmsMessagingTemplate(ActiveMQConnectionFactory connectionFactory) {
        return new JmsMessagingTemplate(connectionFactory);
    }
}
