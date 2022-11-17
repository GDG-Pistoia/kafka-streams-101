package it.gdg.pt.scheduled;

import io.quarkus.scheduler.Scheduled;
import io.smallrye.reactive.messaging.kafka.Record;
import it.gdg.pt.dto.StockValue;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


@Slf4j
@ApplicationScoped
public class StocksJob {

    @Inject
    @Channel("stocks-out")
    Emitter<Record<String, String>> emitter;

    public final static String DATE_FORMAT = "yyyyMMdd";

    public final static String[] TICKERS = {
            "GOOGL",
            "AAPL",
            "MSFT",
            "TSLA"
    };

    @Scheduled(every = "1s")
    public void sendStockValue(){
        final String ticker = pickCompany();
        final Double value = pickStockValue();
        final LocalDateTime now = LocalDateTime.now();

        final StockValue stockValue = StockValue.builder()
                .ticker(ticker)
                .value(value)
                .localDateTime(now)
                .build();

        log.info("operationName=sendStockValueToKafka stockValue={}", stockValue);

        emitter.send(Record.of(buildKey(ticker, now), value.toString())).toCompletableFuture().join();
    }

    public static String pickCompany() {
        return TICKERS[new Random().nextInt(TICKERS.length)];
    }

    public static Double pickStockValue(){
        return ThreadLocalRandom.current().nextDouble(100, 150);
    }

    public static String buildKey(String ticker, LocalDateTime localDateTime) {
        return String.format("%s-%s", ticker.toUpperCase(), format(localDateTime));
    }

    private static String format(LocalDateTime localDateTime){
        return localDateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
}
