package com.aimlessblade.cosmos.geo;

import org.ejml.simple.SimpleMatrix;

import java.util.List;

public class RigidBody implements Entity, Movable {
    private final Pose pose;
    private final List<Vertex> vertexList;
    private final int[] drawOrder;

    public RigidBody(final Pose pose, final List<Vertex> vertexList, final int[] drawOrder) {
        this.pose = pose;
        this.vertexList = vertexList;
        this.drawOrder = drawOrder;
    }

    @Override
    public double[] getVertexData() {
        final int size = getVertexCount() * Vertex.getStride();
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
    public SimpleMatrix getGeoTransform() {
        return pose.toMatrix();
    }

    @Override
    public void evolve(final long dt) {
        pose.evolve(dt);
    }

    @Override
    public void impulse(final double vx, final double vy, final double vz) {
        pose.impulse(vx, vy, vz);
    }

    @Override
    public void angularImpulse(final double vPitch, final double vYaw, final double vRoll) {
        pose.angularImpulse(vPitch, vYaw, vRoll);
    }
}
