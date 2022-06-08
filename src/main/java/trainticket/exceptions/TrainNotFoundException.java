package trainticket.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class TrainNotFoundException extends AbstractThrowableProblem {
    public TrainNotFoundException(long id) {
        super(
                URI.create("/train-not-found"),
                "Train not found",
                Status.NOT_FOUND,
                String.format("Train not found by id: %d", id)
        );
    }
}
