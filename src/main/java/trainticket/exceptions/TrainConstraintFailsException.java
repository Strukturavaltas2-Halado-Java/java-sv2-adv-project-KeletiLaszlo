package trainticket.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class TrainConstraintFailsException extends AbstractThrowableProblem {
    public TrainConstraintFailsException(long id) {
        super(
                URI.create("/api/trains/train-constraint-fail"),
                "Train constraint fail",
                Status.CONFLICT,
                String.format("Cannot delete a train (id: %d) as long as someone has a ticket for it.", id)
        );
    }

    public TrainConstraintFailsException() {
        super(
                URI.create("/api/trains/train-constraint-fail"),
                "Train constraint fail",
                Status.CONFLICT,
                "Cannot delete trains as long as someone has a ticket for it."
        );
    }
}
