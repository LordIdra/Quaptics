package org.metamechanists.death_lasers.connections.links;

public class LinkProperties {
    public static double calculatePower(double inputPower, double maxPower, double powerLoss) {
        return inputPower - ((powerLoss/maxPower) * Math.pow(inputPower, 2));
    }
}
