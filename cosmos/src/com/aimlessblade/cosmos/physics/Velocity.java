package com.aimlessblade.cosmos.physics;

public interface Velocity {
    Displacement overTime(final double secondsElapsed);

    Velocity plus(final Velocity addend);

    Velocity negative();
}
