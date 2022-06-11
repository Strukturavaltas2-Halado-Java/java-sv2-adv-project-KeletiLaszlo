package trainticket.controllers;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import trainticket.dtos.CreateTicketCommand;
import trainticket.dtos.TicketDto;
import trainticket.services.TicketService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@AllArgsConstructor
public class TicketController {

    private TicketService service;

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<TicketDto> listAllTickets() {
        return service.listAllTickets();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TicketDto findTicketById(@PathVariable long id) {
        return service.findTicketById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketDto buyTicket(@Valid @RequestBody CreateTicketCommand createTicketCommand) {
        return service.buyTicket(createTicketCommand);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTicketById(@PathVariable long id) {
        service.deleteTicketById(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllTickets() {
        service.deleteAllTickets();
    }
}
