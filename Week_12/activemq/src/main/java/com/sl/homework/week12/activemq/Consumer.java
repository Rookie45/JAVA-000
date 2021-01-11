package com.sl.homework.week12.activemq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Component
public class Consumer {

    @JmsListener(destination = "my.topic", containerFactory = "jmsListenerContainerTopic")
    public void receiveTopic(String text) {
        System.out.println(" -- topic consumer1:" + text);
    }

    @JmsListener(destination = "my.topic", containerFactory = "jmsListenerContainerTopic")
    public void receiveTopic2(String text) {
        System.out.println(" -- topic consumer2:" + text);
    }

    @JmsListener(destination = "my.queue", containerFactory = "jmsListenerContainerQueue")
    public void receiveQueue(String text) {
        System.out.println(" -- queue consumer:" + text);
    }
}
