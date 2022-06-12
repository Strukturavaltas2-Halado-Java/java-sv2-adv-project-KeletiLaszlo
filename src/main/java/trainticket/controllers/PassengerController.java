package trainticket.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Operations on passengers")
public class PassengerController {

    private PassengerService service;


    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "List all passengers",
            description = "List all passengers.")
    @ApiResponse(responseCode = "202",
            description = "Accepted")
    public List<PassengerDto> listAllPassengers() {
        return service.listAllPassengers();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Find passenger by id",
            description = "Find passenger by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202",
                    description = "Accepted"),
            @ApiResponse(responseCode = "404",
                    description = "Passenger not found")
    })
    public PassengerDto findPassengerById(@PathVariable long id) {
        return service.findPassengerById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create passenger",
            description = "Create new passenger.")
    @ApiResponse(responseCode = "202",
            description = "Accepted")
    public PassengerDto createPassenger(@Valid @RequestBody CreatePassengerCommand createPassengerCommand) {
        return service.createPassenger(createPassengerCommand);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Update passenger by id",
            description = "Update passenger by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202",
                    description = "Accepted"),
            @ApiResponse(responseCode = "404",
                    description = "Passenger not found")
    })
    public PassengerDto modifyPassenger(@PathVariable long id, @Valid @RequestBody ModifyPassengerCommand modifyTrainCommand) {
        return service.modifyPassenger(id, modifyTrainCommand);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete passenger by id",
            description = "Delete passenger by id.")
    @ApiResponse(responseCode = "204",
            description = "No content")
    public void deletePassengerById(@PathVariable long id) {
        service.deletePassengerById(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete all passenger.",
            description = "Delete all passenger, use carefully!")
    @ApiResponse(responseCode = "204",
            description = "No content")
    public void deleteAllPassengers() {
        service.deleteAllPassengers();
    }

}
