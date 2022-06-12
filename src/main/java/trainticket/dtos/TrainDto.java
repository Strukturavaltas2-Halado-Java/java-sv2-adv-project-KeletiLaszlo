package trainticket.dtos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description="train ID", example = "1")
    private Long id;

    @Schema(description="name of the train", example = "PAVA IC")
    private String nameOfTrain;

    @Schema(description="type of the train", example = "INTERCITY")
    private TrainType trainType;

    @Schema(description="departure time", example = "2022-06-30T08:00")
    private LocalDateTime departureTime;

    @Schema(description="departure place", example = "Szolnok")
    private String departurePlace;

    @Schema(description="arrival time", example = "2022-06-30T09:30")
    private LocalDateTime arrivalTime;

    @Schema(description="arrival place", example = "Budapest")
    private String arrivalPlace;

    @Schema(description="distance of the departure and arrival place", example = "100")
    private int distance;

    @JsonBackReference
    @Schema(description="list of the tickets owned by passenger")
    private List<TicketDto> tickets = new ArrayList<>();

}
