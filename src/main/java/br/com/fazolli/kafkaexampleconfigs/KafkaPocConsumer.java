package br.com.fazolli.kafkaexampleconfigs;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaPocConsumer {

    private ApplicationContext ctx;

    private boolean isFirstTime = true;

    public KafkaPocConsumer(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @KafkaListener(topics = "${poc.topic.success}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumerSuccess(final ConsumerRecord consumerRecord, Acknowledgment ack) throws InterruptedException {
        logEvent(consumerRecord, "Fluxo de Sucesso");
        log.info("Fim " + consumerRecord.offset());
        ack.acknowledge();
    }

    @KafkaListener(topics = "${poc.topic.not.commit}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumerSemCommit(final ConsumerRecord consumerRecord, Acknowledgment ack) throws InterruptedException {
        logEvent(consumerRecord, "Fluxo sem comitar a mensagem");
        log.info("Fim " + consumerRecord.offset());
    }

    @KafkaListener(topics = "${poc.topic.error}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumerError(final ConsumerRecord consumerRecord, Acknowledgment ack) throws InterruptedException {
        logEvent(consumerRecord, "Fluxo Error");
        throw new RuntimeException("Erro não tratado");
    }

    @KafkaListener(topics = "${poc.topic.errorhandler}", groupId = "${spring.kafka.consumer.group-id}", errorHandler = "kafkaPocErrorHandler")
    public void consumerErrorHandler(final ConsumerRecord consumerRecord, Acknowledgment ack) throws InterruptedException {
        logEvent(consumerRecord, "Fluxo Error Handler");
        throw new RuntimeException("Erro tratado tratado");
    }

    @KafkaListener(topics = "${poc.topic.crash}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumerCrash(final ConsumerRecord consumerRecord, Acknowledgment ack) throws InterruptedException {
        logEvent(consumerRecord, "Fluxo Error Handler");

        if (isFirstTime) {
            log.info("Set isFirstTime to false");
            isFirstTime = false;
            ack.acknowledge();
        } else {
            System.exit(SpringApplication.exit(ctx, () -> 0));
            //System.exit(0);
        }
    }

    @KafkaListener(topics = "${poc.topic.delay}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumerDelay(final ConsumerRecord consumerRecord, Acknowledgment ack) throws InterruptedException {
        long timeToWait = Long.parseLong(consumerRecord.value().toString());
        logEvent(consumerRecord, "Fluxo de Delay " + timeToWait );
        Thread.sleep(timeToWait);
        log.info("Fim " + consumerRecord.offset());
        ack.acknowledge();
    }

    private void logEvent(ConsumerRecord consumerRecord, String typeEvent) {
        log.info("$$$$$$$$$$$$$$$$ " + typeEvent);
        log.info("key: " + consumerRecord.key());
        log.info("Headers: " + consumerRecord.headers());
        log.info("Partion: " + consumerRecord.partition());
        log.info("Offset: " + consumerRecord.offset());
        log.info("Fluxo: " + consumerRecord.value());
    }
}
