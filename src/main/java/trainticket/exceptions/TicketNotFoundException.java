package trainticket.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class TicketNotFoundException extends AbstractThrowableProblem {
    public TicketNotFoundException(long id) {
        super(
                URI.create("/api/tickets/ticket-not-found"),
                "Ticket not found",
                Status.NOT_FOUND,
                String.format("Ticket not found by id: %d", id)
        );
    }
}
