package it.gdg.pt.streams;

import it.gdg.pt.dto.TickerDateKey;
import it.gdg.pt.entities.LogarithmicReturn;
import lombok.val;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueStore;

import static java.util.Objects.nonNull;

public class StocksProcessor implements Processor<String, String, String, LogarithmicReturn> {

    ProcessorContext<String, LogarithmicReturn> context;
    KeyValueStore<String, Double> store;

    @Override
    public void init(ProcessorContext<String, LogarithmicReturn> context) {
        this.context = context;
        this.store = context.getStateStore(StoreNames.STOCK_PRICE_STORE);
        Processor.super.init(context);
    }

    @Override
    public void process(Record<String, String> record) {
        val key  = record.key();
        val value = record.value();

        if(nonNull(value) && !value.equals("null")){
            val tickerDateKey = TickerDateKey.of(key);
            val ticker = tickerDateKey.getTicker();
            val date = tickerDateKey.getDate();
            val nextPrice = Double.valueOf(value);
            val prevPrice = store.get(ticker);

            if(nonNull(prevPrice)){
                val logarithmicReturn = LogarithmicReturn.builder()
                        .prev(prevPrice)
                        .next(nextPrice)
                        .logReturn(Math.log(nextPrice / prevPrice))
                        .build();

                store.put(ticker, nextPrice);
                context.forward(record.withKey(key).withValue(logarithmicReturn));

            }else {
                val logarithmicReturn = LogarithmicReturn.builder()
                        .next(nextPrice)
                        .build();

                store.put(ticker, nextPrice);
                context.forward(record.withKey(key).withValue(logarithmicReturn));
            }
        }

    }

    @Override
    public void close() {
        Processor.super.close();
    }
}
