package trainticket.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import trainticket.model.TrainType;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateTrainCommand {

    @NotBlank
    @Schema(description="name of the train", example = "Hajdu IC")
    private String nameOfTrain;

    @NotNull
    @Schema(description="type of the train", example = "INTERCITY")
    private TrainType trainType;

    @FutureOrPresent
    @NotNull
    @Schema(description="departure time", example = "2022-06-30T08:00")
    private LocalDateTime departureTime;

    @NotBlank
    @Schema(description="departure place", example = "Szolnok")
    private String departurePlace;

    @FutureOrPresent
    @NotNull
    @Schema(description="arrival time", example = "2022-06-30T09:30")
    private LocalDateTime arrivalTime;

    @NotBlank
    @Schema(description="arrival place", example = "Budapest")
    private String arrivalPlace;

    @Positive
    @NotNull
    @Schema(description="distance of the departure and arrival place", example = "100")
    private int distance;
}
