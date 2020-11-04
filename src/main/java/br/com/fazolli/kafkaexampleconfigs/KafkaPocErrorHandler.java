package br.com.fazolli.kafkaexampleconfigs;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaPocErrorHandler implements KafkaListenerErrorHandler {

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {

        try {
            consumer.commitSync();
            handleError(message,exception);
        } catch (Throwable e) {
            log.error("Erro ao comitar a mensagem");
        }
        return null;
    }

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException e) {

        log.info("$$$ Fluxo de Tratamento de Erro");
        log.info("Headers: " + message.getHeaders());
        log.info("Payload: " + message.getPayload());
        log.info("Fim Fluxo de Erro");

        return null;
    }
}
