package com.aimlessblade.cosmos.graphics.vertex;

import com.aimlessblade.cosmos.graphics.Entity;
import org.ejml.simple.SimpleMatrix;

import java.util.List;

public class VertexListEntity implements Entity {
    private final double[] vertexData;
    private final int[] elementData;
    private final int vertexCount;

    public VertexListEntity(final List<Vertex> vertexList, final int[] elementData, final int vertexLength) {
        this.elementData = elementData;

        vertexCount = vertexList.size();
        vertexData = new double[vertexCount * vertexLength];

        int i = 0;
        for (final Vertex vertex : vertexList) {
            for (final double d : vertex.data()) {
                vertexData[i] = d;
                i++;
            }
        }
    }

    @Override
    public double[] getVertexData() {
        return vertexData;
    }

    @Override
    public int getVertexCount() {
        return vertexCount;
    }

    @Override
    public int[] getElementData() {
        return elementData;
    }

    @Override
    public SimpleMatrix toMatrix() {
        return SimpleMatrix.identity(4);
    }
}
