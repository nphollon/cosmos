package com.aimlessblade.cosmos.graphics.vertex;

import com.aimlessblade.cosmos.graphics.Entity;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.ejml.simple.SimpleMatrix;

import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode
@ToString
public class VertexListEntity implements Entity {
    private final List<Double> vertexData;
    private final List<Integer> elementData;
    private final VertexType vertexType;
    private final int vertexCount;

    public VertexListEntity(final VertexType vertexType, final List<Vertex> vertexList, final List<Integer> elementData) {
        this.elementData = elementData;
        this.vertexType = vertexType;
        vertexCount = vertexList.size();
        vertexData = flattenVertexList(vertexList);
    }

    private List<Double> flattenVertexList(final List<Vertex> vertexList) {
        return vertexList.stream().flatMap(v -> v.data().stream()).collect(Collectors.toList());
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
    public VertexType getVertexType() {
        return vertexType;
    }

    @Override
    public SimpleMatrix toMatrix() {
        return SimpleMatrix.identity(4);
    }
}
