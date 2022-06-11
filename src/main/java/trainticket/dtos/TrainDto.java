package trainticket.dtos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import trainticket.model.TrainType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrainDto {
    private Long id;

    private String nameOfTrain;

    private TrainType trainType;

    private LocalDateTime departureTime;

    private String departurePlace;

    private LocalDateTime arrivalTime;

    private String arrivalPlace;

    private int distance;

    @JsonBackReference
    private List<TicketDto> tickets = new ArrayList<>();

}
