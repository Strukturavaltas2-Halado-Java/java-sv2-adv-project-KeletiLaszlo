package trainticket.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class InvalidPeriodTimeGivenException extends AbstractThrowableProblem {
    public InvalidPeriodTimeGivenException(int plusHours, int plusMinutes) {
        super(
                URI.create("/api/discounts/invalid-age-given"),
                "Invalid period times given.",
                Status.BAD_REQUEST,
                String.format("The plus hours(%d) and the plus minutes(%d) cannot be zero at the same time.", plusHours, plusMinutes)
        );
    }
}
