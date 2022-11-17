package it.gdg.pt.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class StockValue {
    private String ticker;
    private Double value;
    private LocalDateTime localDateTime;
}
