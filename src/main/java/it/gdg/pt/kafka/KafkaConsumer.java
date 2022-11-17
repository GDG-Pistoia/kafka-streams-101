package it.gdg.pt.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.annotations.Blocking;
import it.gdg.pt.control.LogarithmicReturnControl;
import it.gdg.pt.entities.LogarithmicReturn;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Slf4j
@ApplicationScoped
public class KafkaConsumer {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    LogarithmicReturnControl logarithmicReturnControl;


    @Blocking
    @Transactional
    @Incoming("logarithmic-return-exporter")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    public CompletionStage<Void> readAndSaveLogarithmicReturn(ConsumerRecord<String, String> record) {
        if(record.value() != null){
            val logarithmicReturn = deserialize(record.value(), LogarithmicReturn.class);
            logarithmicReturnControl.saveOrUpdate(logarithmicReturn);;
        }
        return CompletableFuture.completedFuture(null);
    }

    private <T> T deserialize(String value, Class<T> clazz){
        try {
            return objectMapper.readValue(value, clazz);
        } catch (JsonProcessingException e) {
            log.error("operationName=entityDeserialization value={}", value);
            throw new RuntimeException(e);
        }
    }
}
