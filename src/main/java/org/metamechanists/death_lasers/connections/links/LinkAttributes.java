package org.metamechanists.death_lasers.connections.links;

public class LinkAttributes {
    public static double powerLoss(double inputPower, double maxPower, double powerLoss) {
        return inputPower - ((powerLoss/maxPower) * Math.pow(inputPower, 2));
    }
}
