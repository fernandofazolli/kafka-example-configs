package br.com.fazolli.kafkaexampleconfigs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Component
@Slf4j
public class KafkaPocProducer {
    @Value("${poc.topic.success}")
    private String successTopic;

    @Value("${poc.topic.not.commit}")
    private String notCommitTopic;

    @Value("${poc.topic.error}")
    private String errorTopic;

    @Value("${poc.topic.errorhandler}")
    private String handlerTopic;

    @Value("${poc.topic.crash}")
    private String crashTopic;

    @Value("${poc.topic.delay}")
    private String delayTopic;

    private final KafkaTemplate kafkaTemplate;

    public KafkaPocProducer(final KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(final @RequestBody String fluxo) {
        final String mensageKey = UUID.randomUUID().toString();
        switch (fluxo) {
            case "success": {
                kafkaTemplate.send(successTopic, mensageKey, fluxo);
                break;
            }
            case "notCommit": {
                kafkaTemplate.send(notCommitTopic, mensageKey, fluxo);
                break;
            }
            case "error": {
                kafkaTemplate.send(errorTopic, mensageKey, fluxo);
                break;
            }
            case "handler": {
                kafkaTemplate.send(handlerTopic, mensageKey, fluxo);
                break;
            }
            case "crash": {
                kafkaTemplate.send(crashTopic, mensageKey, fluxo);
                break;
            }
            default:
                try {
                    Long delay = Long.parseLong(fluxo);
                    kafkaTemplate.send(delayTopic, mensageKey, fluxo);
                } catch (Throwable e){
                    log.info("Opção Errada");
                }
        }


    }
}

