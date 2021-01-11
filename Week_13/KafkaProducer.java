package com.sl.homework.week13.springkafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@EnableScheduling
public class KafkaProducer {

    private static final AtomicInteger topicId = new AtomicInteger(0);

    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducer(@Qualifier(value = "producerKafkaTemplate") KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 2000)
    public void send() {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send("my.kafka.sl", String.valueOf(topicId.incrementAndGet()),"hi, my kafka ");

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("[KafkaProducer][send] send failure", ex);
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("[KafkaProducer][send] send success, result:{}", result);
            }
        });

    }
}
