package trainticket.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class TrainConstraintFailsException extends AbstractThrowableProblem {
    public TrainConstraintFailsException(long id) {
        super(
                URI.create("/api/trains/train-constraint-fail"),
                "Train constraint fail",
                Status.IM_USED,
                String.format("Cannot delete a train (id: %d) as long as someone has a ticket for it.", id)
        );
    }
}
