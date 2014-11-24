package com.aimlessblade.cosmos.graphics;

import com.aimlessblade.cosmos.physics.Movable;
import com.aimlessblade.cosmos.physics.Velocity;
import org.ejml.simple.SimpleMatrix;

import java.util.List;

public final class RigidBody implements Entity, Movable {
    private final Movable pose;
    private final Entity vertexEntity;

    public RigidBody(final Movable pose, final Entity vertexEntity) {
        this.pose = pose;
        this.vertexEntity = vertexEntity;
    }

    @Override
    public List<Double> getVertexData() {
        return vertexEntity.getVertexData();
    }

    @Override
    public int getVertexCount() {
        return vertexEntity.getVertexCount();
    }

    @Override
    public int[] getElementData() {
        return vertexEntity.getElementData();
    }

    @Override
    public SimpleMatrix toMatrix() {
        return pose.toMatrix();
    }

    @Override
    public void evolve(final double dt) {
        pose.evolve(dt);
    }

    @Override
    public void impulse(final Velocity velocity) {
        pose.impulse(velocity);
    }

    @Override
    public void angularImpulse(final Velocity angularVelocity) {
        pose.angularImpulse(angularVelocity);
    }
}
