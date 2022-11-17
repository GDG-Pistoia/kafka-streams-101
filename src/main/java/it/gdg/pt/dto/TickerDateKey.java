package it.gdg.pt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static it.gdg.pt.scheduled.StocksJob.DATE_FORMAT;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class TickerDateKey {

    private String ticker;
    private LocalDate date;


    public static TickerDateKey of(String key){
        String[] keySplit = key.split("-");
        if(keySplit.length == 2){
            return new TickerDateKey(keySplit[0], LocalDate
                    .parse(keySplit[1],
                            DateTimeFormatter.ofPattern(DATE_FORMAT)
                                    .withLocale(Locale.US )
                    ));
        }
        return new TickerDateKey(keySplit[0], null);
    }
}
