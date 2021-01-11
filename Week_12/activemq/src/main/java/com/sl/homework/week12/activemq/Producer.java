package com.sl.homework.week12.activemq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import javax.jms.Topic;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@EnableScheduling
public class Producer {

    private static final AtomicInteger queueId = new AtomicInteger(0);
    private static final AtomicInteger topicId = new AtomicInteger(0);

    private Queue queue;
    private Topic topic;
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    public Producer(Queue queue, Topic topic, JmsMessagingTemplate messagingTemplate) {
        this.queue = queue;
        this.topic = topic;
        this.jmsMessagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedDelay = 2000)
    public void send() {
        this.jmsMessagingTemplate.convertAndSend(this.queue, "hi, my queue, index="+queueId.incrementAndGet());
        this.jmsMessagingTemplate.convertAndSend(this.topic, "hi, my topic, index="+topicId.incrementAndGet());
    }
}
