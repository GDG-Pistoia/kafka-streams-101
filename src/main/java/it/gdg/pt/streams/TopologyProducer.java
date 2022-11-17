package it.gdg.pt.streams;

import io.quarkus.kafka.client.serialization.ObjectMapperSerde;
import it.gdg.pt.entities.LogarithmicReturn;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Topology;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import static org.apache.kafka.streams.state.Stores.inMemoryKeyValueStore;
import static org.apache.kafka.streams.state.Stores.keyValueStoreBuilder;

@Slf4j
@ApplicationScoped
public class TopologyProducer {

    public static final String STOCKS_SOURCE = "stocksSource";
    public static final String STOCKS_TOPIC = "stocks";
    public static final String LOGARITHMIC_RETURN_TOPIC = "logarithmic-returns";
    public static final String LOGARITHMIC_RETURN_SINK = "logarithmicReturnSink";
    public static final String STOCK_PROCESSOR = StocksProcessor.class.getSimpleName();

    @Produces
    public Topology buildTopology(){

        ObjectMapperSerde<LogarithmicReturn> logarithmicReturnSerde = new ObjectMapperSerde<>(LogarithmicReturn.class);

        final Topology topology = new Topology();
        topology.addStateStore(keyValueStoreBuilder(inMemoryKeyValueStore(StoreNames.STOCK_PRICE_STORE), Serdes.String(), Serdes.Double()));
        topology.addSource(STOCKS_SOURCE, Serdes.String().deserializer(), Serdes.String().deserializer(), STOCKS_TOPIC);
        topology.addProcessor(STOCK_PROCESSOR, StocksProcessor::new, STOCKS_SOURCE);
        topology.connectProcessorAndStateStores(STOCK_PROCESSOR, StoreNames.STOCK_PRICE_STORE);
        topology.addSink(LOGARITHMIC_RETURN_SINK, LOGARITHMIC_RETURN_TOPIC, Serdes.String().serializer(), logarithmicReturnSerde.serializer(), STOCK_PROCESSOR);

        System.out.println(topology.describe());

        return topology;
    }
}
