package trainticket.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class PassengerConstraintFailsException extends AbstractThrowableProblem {
    public PassengerConstraintFailsException(long id) {
        super(
                URI.create("/api/passengers/passenger-constraint-fail"),
                "Passenger constraint fail",
                Status.CONFLICT,
                String.format("Cannot delete a passenger (id: %d) until it has tickets.", id)
        );
    }

    public PassengerConstraintFailsException() {
        super(
                URI.create("/api/passengers/passenger-constraint-fail"),
                "Passenger constraint fail",
                Status.CONFLICT,
                "Cannot delete passengers until it has tickets."
        );
    }
}
