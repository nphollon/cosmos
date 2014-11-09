package com.aimlessblade.cosmos.geo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ejml.simple.SimpleMatrix;

@Getter
@AllArgsConstructor(staticName = "build")
public class Pose implements Movable {
    private Displacement displacement;
    private Orientation orientation;

    private void move(final double dx, final double dy, final double dz) {
        displacement = displacement.plus(Displacement.cartesian(dx, dy, dz));
    }

    private void rotate(final double x, final double y, final double z, final double angle) {
        orientation = orientation.times(Orientation.axisAngle(x, y, z, angle));
    }

    public SimpleMatrix toMatrix() {
        return SimpleMatrix.identity(4);
    }

    @Override
    public void evolve(final long dt) {

    }

    @Override
    public void impulse(final double vx, final double vy, final double vz) {

    }

    @Override
    public void angularImpulse(final double vPitch, final double vYaw, final double vRoll) {

    }
}
