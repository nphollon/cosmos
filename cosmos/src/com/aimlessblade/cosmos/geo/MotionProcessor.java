package com.aimlessblade.cosmos.geo;

import com.aimlessblade.cosmos.app.Processor;
import lombok.Getter;
import org.joda.time.DateTimeUtils;

import java.util.List;

@Getter
public final class MotionProcessor implements Processor {
    private final List<Movable> movables;
    private long lastUpdateTime;

    public MotionProcessor(final List<Movable> movables) {
        this.movables = movables;
        lastUpdateTime = DateTimeUtils.currentTimeMillis();
    }

    @Override
    public void run() {
        final long currentTime = DateTimeUtils.currentTimeMillis();
        final long dt = currentTime - lastUpdateTime;
        lastUpdateTime = DateTimeUtils.currentTimeMillis();
        movables.stream().forEach(m -> m.evolve(dt));
    }
}
