package trainticket.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class DiscountNotFoundException extends AbstractThrowableProblem {
    public DiscountNotFoundException(long id) {
        super(
                URI.create("/api/discounts/discount-not-found"),
                "Discount not found!",
                Status.NOT_FOUND,
                String.format("Discount not found by id: %d", id)
        );
    }
}
