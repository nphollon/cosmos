package com.aimlessblade.cosmos.geo;

import org.joda.time.DateTimeUtils;

import java.util.List;

public class Puppeteer {
    private final List<Pose> entities;
    private long lastUpdateTime;

    public Puppeteer(final List<Pose> entities) {
        lastUpdateTime = DateTimeUtils.currentTimeMillis();
        this.entities = entities;
    }

    public void event(final int eventKey, final boolean eventKeyState) {
        // process keyboard events
    }

    public void move() {
        long currentTime = DateTimeUtils.currentTimeMillis();
        long dt = currentTime - lastUpdateTime;

        for (Pose entity : entities) {
            entity.evolve(dt);
        }

        lastUpdateTime = currentTime;
    }
}
