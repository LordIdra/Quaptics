package org.metamechanists.death_lasers.lasers;

public class SpawnTimer {
    private final int interval;
    private int tick = 0;

    public SpawnTimer(int interval) {
        this.interval = interval;
    }

    public boolean Update() {
        tick++;

        if (tick >= interval) {
            tick = 0;
            return true;
        }

        return false;
    }
}
