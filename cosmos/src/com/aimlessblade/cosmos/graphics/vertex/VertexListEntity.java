package com.aimlessblade.cosmos.graphics.vertex;

import com.aimlessblade.cosmos.graphics.Entity;
import org.ejml.simple.SimpleMatrix;

import java.util.List;
import java.util.stream.Collectors;

public class VertexListEntity implements Entity {
    private final List<Double> vertexData;
    private final List<Integer> elementData;
    private final int vertexCount;

    public VertexListEntity(final List<Vertex> vertexList, final List<Integer> elementData) {
        this.elementData = elementData;
        vertexCount = vertexList.size();
        vertexData = vertexList.stream().flatMap(v -> v.data().stream()).collect(Collectors.toList());
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
    public List<Integer> getElementData() {
        return elementData;
    }

    @Override
    public SimpleMatrix toMatrix() {
        return SimpleMatrix.identity(4);
    }
}
