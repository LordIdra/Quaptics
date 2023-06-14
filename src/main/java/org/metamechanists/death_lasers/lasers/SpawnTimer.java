package org.metamechanists.death_lasers.lasers;

public class SpawnTimer {
    private final int interval;
    private int tick;

    public SpawnTimer(int interval) {
        this.interval = interval;
        this.tick = interval;
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
