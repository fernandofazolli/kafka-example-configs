package br.com.fazolli.kafkaexampleconfigs;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderConsumer {

//    @KafkaListener(topics = "${order.topic}", groupId = "${spring.kafka.consumer.group-id}")
//    public void consumer(String order) {
//        System.out.println("Order: " + order);
//    }

    @KafkaListener(topics = "${order.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumer(final ConsumerRecord consumerRecord, Acknowledgment ack) throws InterruptedException {
        System.out.println("key: " + consumerRecord.key());
        System.out.println("Headers: " + consumerRecord.headers());
        System.out.println("Partion: " + consumerRecord.partition());
        System.out.println("Offset: " + consumerRecord.offset());
        System.out.println("Order: " + consumerRecord.value());
        //Thread.sleep(Long.parseLong(consumerRecord.value().toString()));
        System.out.println("Fim " + consumerRecord.offset());
        ack.acknowledge();
    }
}
