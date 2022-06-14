package trainticket.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class PassengerConstraintFailsException extends AbstractThrowableProblem {
    public PassengerConstraintFailsException(long id) {
        super(
                URI.create("/api/passengers/passenger-constraint-fail"),
                "Passenger constraint fail",
                Status.IM_USED,
                String.format("Cannot delete a passenger (id: %d) until it has tickets.", id)
        );
    }
}
