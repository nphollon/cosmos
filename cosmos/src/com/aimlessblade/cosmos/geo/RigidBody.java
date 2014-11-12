package com.aimlessblade.cosmos.geo;

import org.ejml.simple.SimpleMatrix;

import java.util.List;

public final class RigidBody implements Entity, Movable {
    private final Movable pose;
    private final List<Vertex> vertexList;
    private final int[] drawOrder;

    public RigidBody(final Movable pose, final List<Vertex> vertexList, final int[] drawOrder) {
        this.pose = pose;
        this.vertexList = vertexList;
        this.drawOrder = drawOrder;
    }

    @Override
    public double[] getVertexData() {
        final int size = getVertexCount() * Vertex.getLength();
        final double[] data = new double[size];

        int i = 0;
        for (final Vertex vertex : vertexList) {
            for (final Double d : vertex.data()) {
                data[i] = d;
                i++;
            }
        }

        return data;
    }

    @Override
    public int getVertexCount() {
        return vertexList.size();
    }

    @Override
    public int[] getElementData() {
        return drawOrder;
    }

    @Override
    public SimpleMatrix getTransform() {
        return toMatrix();
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
