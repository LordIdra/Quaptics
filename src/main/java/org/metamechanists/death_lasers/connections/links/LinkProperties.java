package org.metamechanists.death_lasers.connections.links;

public class LinkProperties {
    public static double calculatePower(Link inputLink, double maxPower, double powerLoss) {
        final double inputPower = inputLink.getPower();
        return inputPower - ((powerLoss/maxPower) * Math.pow(inputPower, 2));
    }
}
