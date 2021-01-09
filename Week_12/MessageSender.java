package com.sl.homework.week12.activemq.mq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

public class MessageSender {
    // url tcp://localhost:61616
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

    public static void main(String[] args) throws JMSException {
        // 获取mq的连接并启动它
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        // 创建(非事务的)会话
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 在JMS Server上创建queue，名字为sl.queue
        Destination destination = session.createQueue("sl.Queue");
//        session.createTopic("sl.topic");
        // 向queue发送消息的producer
        MessageProducer producer = session.createProducer(destination);
        // 创建发送的消息对象，此处为文本消息，还可以是byte，Map，Object，Stream
        TextMessage message = session.createTextMessage("Hello,shili");
        producer.send(message);

        System.out.println("Send message '" + message.getText() + "'");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        session.close();
        connection.close();
    }
}
