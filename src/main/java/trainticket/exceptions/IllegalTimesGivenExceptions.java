package trainticket.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;
import java.time.LocalDateTime;

public class IllegalTimesGivenExceptions extends AbstractThrowableProblem {
    public IllegalTimesGivenExceptions(LocalDateTime departureTime, LocalDateTime arrivalTime) {
        super(
                URI.create("/api/trains/invalid_times_given"),
                "Invalid times given.",
                Status.BAD_REQUEST,
                String.format("The departure time (%s) must be earlier than arrival time(%s).", departureTime.toString(), arrivalTime.toString())
        );
    }
}
