package trainticket.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import trainticket.dtos.CreateTicketCommand;
import trainticket.dtos.TicketDto;
import trainticket.model.services.TicketService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@AllArgsConstructor
@Tag(name = "Operations on tickets")
public class TicketController {

    private TicketService service;

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "List all tickets",
            description = "List all tickets.")
    @ApiResponse(responseCode = "202",
            description = "Accepted")
    public List<TicketDto> listAllTickets() {
        return service.listAllTickets();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Find ticket by id",
            description = "Find ticket by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202",
                    description = "Accepted"),
            @ApiResponse(responseCode = "404",
                    description = "Ticket not found")
    })
    public TicketDto findTicketById(@PathVariable long id) {
        return service.findTicketById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Buy ticket",
            description = "Buy ticket.")
    @ApiResponse(responseCode = "201",
            description = "Created")
    public TicketDto buyTicket(@Valid @RequestBody CreateTicketCommand createTicketCommand) {
        return service.buyTicket(createTicketCommand);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete ticket by id",
            description = "Delete ticket by id.")
    @ApiResponse(responseCode = "204",
            description = "No content")
    public void deleteTicketById(@PathVariable long id) {
        service.deleteTicketById(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete all ticket.",
            description = "Delete all ticket, use carefully!")
    @ApiResponse(responseCode = "204",
            description = "No content")
    public void deleteAllTickets() {
        service.deleteAllTickets();
    }
}
