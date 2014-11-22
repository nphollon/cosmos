package com.aimlessblade.cosmos.physics;

interface Orientation extends ToMatrix {
    Orientation rotate(final Displacement rotation);
}
