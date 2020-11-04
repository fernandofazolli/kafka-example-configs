package br.com.fazolli.kafkaexampleconfigs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/sendTo")
@Slf4j
public class KafkaPocController {

    private final KafkaPocProducer kafkaPocProducer;

    public KafkaPocController(KafkaPocProducer kafkaPocProducer) {
        this.kafkaPocProducer = kafkaPocProducer;
    }

    @PostMapping()
    public void send(@RequestBody String event) {
        kafkaPocProducer.send(event);
    }
}
