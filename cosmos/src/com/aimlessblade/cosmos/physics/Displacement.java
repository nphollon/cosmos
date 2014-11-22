package com.aimlessblade.cosmos.physics;

public interface Displacement extends ToMatrix {
    Displacement plus(final Displacement addend);

    double getX();
    double getY();
    double getZ();

    double getMagnitude();
}
