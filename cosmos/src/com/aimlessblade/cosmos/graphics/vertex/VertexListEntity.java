package com.aimlessblade.cosmos.graphics.vertex;

import com.aimlessblade.cosmos.graphics.Entity;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.List;

public class VertexListEntity implements Entity {
    private final List<Double> vertexData;
    private final int[] elementData;
    private final int vertexCount;

    public VertexListEntity(final List<Vertex> vertexList, final int[] elementData) {
        this.elementData = elementData;
        vertexCount = vertexList.size();

        vertexData = new ArrayList<>();

        for (final Vertex vertex : vertexList) {
            vertexData.addAll(vertex.data());
        }
    }

    @Override
    public List<Double> getVertexData() {
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
