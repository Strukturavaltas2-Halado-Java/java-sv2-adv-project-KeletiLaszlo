package trainticket.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class IllegalAgeGivenExceptions extends AbstractThrowableProblem {
    public IllegalAgeGivenExceptions(int age) {
        super(
                URI.create("/api/discounts/invalid-age-given"),
                "Invalid age given.",
                Status.BAD_REQUEST,
                String.format("The age must be positive or zero (%d).", age)
        );
    }
}
