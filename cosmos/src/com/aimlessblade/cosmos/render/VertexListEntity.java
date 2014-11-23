package com.aimlessblade.cosmos.render;

import org.ejml.simple.SimpleMatrix;

import java.util.List;

public class VertexListEntity implements Entity {
    private final List<Vertex> vertexList;
    private final int[] drawOrder;

    public VertexListEntity(final List<Vertex> vertexList, final int[] drawOrder) {
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
    public SimpleMatrix toMatrix() {
        return SimpleMatrix.identity(4);
    }
}
