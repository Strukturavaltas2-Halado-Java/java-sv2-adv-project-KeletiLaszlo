package trainticket.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class PassengerNotFoundException extends AbstractThrowableProblem {
    public PassengerNotFoundException(long id) {
        super(
                URI.create("/api/passengers/passenger-not-found"),
                "Passenger not found!",
                Status.NOT_FOUND,
                String.format("Passenger not found by id: %d", id)
        );
    }
}
