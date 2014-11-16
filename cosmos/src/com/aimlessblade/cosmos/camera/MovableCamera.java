package com.aimlessblade.cosmos.camera;

import com.aimlessblade.cosmos.physics.Movable;
import com.aimlessblade.cosmos.physics.Velocity;
import org.ejml.simple.SimpleMatrix;

public class MovableCamera implements Camera, Movable {
    private final Camera staticCamera;
    private final Movable pose;

    public MovableCamera(Camera staticCamera, Movable pose) {
        this.staticCamera = staticCamera;
        this.pose = pose;
    }


    @Override
    public SimpleMatrix getPerspective() {
        return staticCamera.getPerspective().mult(pose.toMatrix());
    }

    @Override
    public void evolve(double dt) {
        pose.evolve(dt);
    }

    @Override
    public void impulse(Velocity velocity) {
        pose.impulse(velocity.negative());
    }

    @Override
    public void angularImpulse(Velocity angularVelocity) {
        pose.angularImpulse(angularVelocity.negative());
    }

    @Override
    public SimpleMatrix toMatrix() {
        return pose.toMatrix();
    }
}
