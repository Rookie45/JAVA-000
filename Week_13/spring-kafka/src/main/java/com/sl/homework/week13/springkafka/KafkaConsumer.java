package com.sl.homework.week13.springkafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {

    @KafkaListener(topics = {"my.kafka.sl"}, containerFactory = "kafkaListenerContainerFactory")
    public void receive(ConsumerRecord<String, String> consumerRecord) {
        log.info("[KafkaConsumer][receive] =============receive message!==============");
        String topic = consumerRecord.topic();
        String key = consumerRecord.key();
        String value = consumerRecord.value();
        int partition = consumerRecord.partition();

        StringBuilder sb = new StringBuilder();
        sb.append("topic:").append(topic)
                .append(" key:").append(key)
                .append(" value:").append(value)
                .append(" partition:").append(partition);
        log.info("[KafkaConsumer][receive] record include: {}",sb);
    }
}
