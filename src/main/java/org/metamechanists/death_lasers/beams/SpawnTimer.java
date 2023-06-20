package org.metamechanists.death_lasers.beams;

public class SpawnTimer {
    private final int interval;
    private int tick;

    public SpawnTimer(int interval) {
        this.interval = interval;
        this.tick = interval;
    }

    public boolean update() {
        tick++;

        if (tick >= interval) {
            tick = 0;
            return true;
        }

        return false;
    }
}
