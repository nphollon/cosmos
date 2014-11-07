package com.aimlessblade.cosmos.geo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ejml.simple.SimpleMatrix;

@Getter
@AllArgsConstructor(staticName = "build")
public class Pose implements Movable {
    private Displacement displacement;
    private Orientation orientation;

    public void moveLeft(final double delta) {
        move(delta, 0, 0);
    }

    public void moveUp(final double delta) {
        move(0, delta, 0);
    }

    public void moveForward(final double delta) {
        move(0, 0, delta);
    }

    public void adjustPitch(final double delta) {
        rotate(1, 0, 0, delta);
    }

    public void adjustYaw(final double delta) {
        rotate(0, 1, 0, delta);
    }

    public void adjustRoll(final double delta) {
        rotate(0, 0, 1, delta);
    }

    private void move(final double dx, final double dy, final double dz) {
        displacement = displacement.plus(Displacement.cartesian(dx, dy, dz));
    }

    private void rotate(final double x, final double y, final double z, final double angle) {
        orientation = orientation.times(Orientation.axisAngle(x, y, z, angle));
    }

    public SimpleMatrix toMatrix() {
        return null;
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
