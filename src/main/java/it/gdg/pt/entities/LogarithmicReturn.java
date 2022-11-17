package it.gdg.pt.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class LogarithmicReturn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double prev;
    private Double next;
    private Double logReturn;
    private LocalDateTime emitted_at;
    private LocalDateTime inserted_at;
}
