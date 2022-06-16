package trainticket.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import trainticket.dtos.CreateTrainCommand;
import trainticket.dtos.ModifyTrainCommand;
import trainticket.dtos.TrainDto;
import trainticket.services.TrainService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trains")
@AllArgsConstructor
@Tag(name = "Operations on trains")
public class TrainController {
    private TrainService service;

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "List all trains",
            description = "List all trains.")
    @ApiResponse(responseCode = "202",
            description = "Accepted")
    public List<TrainDto> listAllTrains(
            @Parameter(name = "departurePlace", description = "The place where the traveling starts.",
                    example = "Szolnok")
            @RequestParam Optional<String> departurePlace,
            @Parameter(name = "arrivalPlace", description = "The place where the traveling ends.",
                    example = "Budapest")
            @RequestParam Optional<String> arrivalPlace,
            @Parameter(name = "departureTime", description = "The time when the travel is planed.",
                    example = "2023-01-01T09:00")
            @RequestParam("departureTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Optional<LocalDateTime> departureTime) {
        return service.listAllTrains(departurePlace, arrivalPlace, departureTime);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Find train by id",
            description = "Find train by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202",
                    description = "Accepted"),
            @ApiResponse(responseCode = "404",
                    description = "Train not found")
    })
    public TrainDto findTrainById(@PathVariable long id) {
        return service.findTrainById(id);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create train",
            description = "Create new train.")
    @ApiResponse(responseCode = "201",
            description = "Created")
    public TrainDto createTrain(@Valid @RequestBody CreateTrainCommand createTrainCommand) {
        return service.createTrain(createTrainCommand);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Update train by id",
            description = "Update train by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202",
                    description = "Accepted"),
            @ApiResponse(responseCode = "404",
                    description = "Train not found")
    })
    public TrainDto modifyTrain(@PathVariable long id, @Valid @RequestBody ModifyTrainCommand modifyTrainCommand) {
        return service.modifyTrain(id, modifyTrainCommand);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete train by id",
            description = "Delete train by id.")
    @ApiResponse(responseCode = "204",
            description = "No content")
    public void deleteTrainById(@PathVariable long id) {
        service.deleteTrainById(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete all train.",
            description = "Delete all train, use carefully!")
    @ApiResponse(responseCode = "204",
            description = "No content")
    public void deleteAllTrains() {
        service.deleteAllTrains();
    }
}
