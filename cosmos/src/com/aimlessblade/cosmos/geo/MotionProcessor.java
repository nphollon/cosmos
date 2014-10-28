package com.aimlessblade.cosmos.geo;

import com.aimlessblade.cosmos.app.Processor;
import lombok.Getter;
import org.joda.time.DateTimeUtils;

import java.util.List;

@Getter
public class MotionProcessor implements Processor {
    private final List<Movable> movables;
    private long lastUpdateTime;

    public MotionProcessor(final List<Movable> movables) {
        this.movables = movables;
        lastUpdateTime = DateTimeUtils.currentTimeMillis();
    }

    @Override
    public void run() {
        long currentTime = DateTimeUtils.currentTimeMillis();
        long dt = currentTime - lastUpdateTime;
        lastUpdateTime = DateTimeUtils.currentTimeMillis();

        for (Movable movable : movables) {
            movable.evolve(dt);
        }
    }
}
