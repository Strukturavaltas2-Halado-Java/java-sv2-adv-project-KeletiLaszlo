package trainticket.model;

import lombok.Getter;

@Getter
public enum TrainType {
    PASSENGER_TRAIN(1), FAST_TRAIN(1.2), INTERCITY(1.5);

    private final double priceMultiplier;

    TrainType(double priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }
}
