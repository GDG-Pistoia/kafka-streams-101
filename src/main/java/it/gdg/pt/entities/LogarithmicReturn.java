package it.gdg.pt.entities;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class LogarithmicReturn {

    @EmbeddedId
    private LogarithmicReturnId id;
    private Double prev;
    private Double next;
    private Double logReturn;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    @ToString
    public static class LogarithmicReturnId implements Serializable {
        private String ticker;
        private LocalDate date;
    }
}
