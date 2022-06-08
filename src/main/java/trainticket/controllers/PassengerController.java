package trainticket.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import trainticket.dtos.CreatePassengerCommand;
import trainticket.dtos.ModifyPassengerCommand;
import trainticket.dtos.PassengerDto;
import trainticket.services.PassengerService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/passengers")
@AllArgsConstructor
public class PassengerController {

    private PassengerService service;


    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<PassengerDto> listAllPassengers() {
        return service.listAllPassengers();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public PassengerDto findPassengerById(@PathVariable long id) {
        return service.findPassengerById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PassengerDto createPassenger(@Valid @RequestBody CreatePassengerCommand createPassengerCommand) {
        return service.createPassenger(createPassengerCommand);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public PassengerDto modifyPassenger(@PathVariable long id, @Valid @RequestBody ModifyPassengerCommand modifyTrainCommand) {
        return service.modifyPassenger(id, modifyTrainCommand);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePassengerById(@PathVariable long id) {
        service.deletePassengerById(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllPassengers() {
        service.deleteAllPassengers();
    }

}
