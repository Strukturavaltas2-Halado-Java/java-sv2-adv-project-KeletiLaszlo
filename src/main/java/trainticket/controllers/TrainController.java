package trainticket.controllers;

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
public class TrainController {
    private TrainService service;

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<TrainDto> listAllTrains(@RequestParam Optional<String> departurePlace, @RequestParam Optional<String> arrivalPlace,
                                        @RequestParam("departureTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Optional<LocalDateTime> departureTime) {
        return service.listAllTrains(departurePlace, arrivalPlace, departureTime);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TrainDto findTrainById(@PathVariable long id) {
        return service.findTrainById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrainDto createTrain(@Valid @RequestBody CreateTrainCommand createTrainCommand) {
        return service.createTrain(createTrainCommand);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TrainDto modifyTrain(@PathVariable long id, @Valid @RequestBody ModifyTrainCommand modifyTrainCommand) {
        return service.modifyTrain(id, modifyTrainCommand);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrainById(@PathVariable long id) {
        service.deleteTrainById(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllTrains() {
        service.deleteAllTrains();
    }
}
