package com.aimlessblade.cosmos.geo;

import com.aimlessblade.cosmos.app.Processor;
import lombok.AccessLevel;
import lombok.Getter;
import org.joda.time.DateTimeUtils;

import java.util.List;

@Getter(AccessLevel.PACKAGE)
public final class MotionProcessor implements Processor {
    private static final double CONVERT_TO_SECONDS = 1e-3;
    private final List<Movable> movables;
    private long lastUpdateTime;

    public MotionProcessor(final List<Movable> movables) {
        this.movables = movables;
        lastUpdateTime = DateTimeUtils.currentTimeMillis();
    }

    @Override
    public void run() {
        final long currentTime = DateTimeUtils.currentTimeMillis();
        final double dt = (currentTime - lastUpdateTime) * CONVERT_TO_SECONDS;
        lastUpdateTime = currentTime;
        movables.stream().forEach(m -> m.evolve(dt));
    }
}
