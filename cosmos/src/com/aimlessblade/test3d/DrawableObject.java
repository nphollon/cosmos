package com.aimlessblade.test3d;

import lombok.Getter;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.List;

public class DrawableObject {
    @Getter private final List<Integer> drawOrder;
    @Getter private final List<Vertex> vertices;
    @Getter private final SimpleMatrix geoTransform;

    public DrawableObject(List<Vertex> vertices, List<Integer> drawOrder) {
        this.vertices = new ArrayList<>(vertices);
        this.drawOrder = new ArrayList<>(drawOrder);

        geoTransform = SimpleMatrix.identity(4);
    }

    public void setOffset(double x, double y, double z) {
        geoTransform.set(0, 3, x);
        geoTransform.set(1, 3, y);
        geoTransform.set(2, 3, z);
    }

    public int vertexCount() {
        return vertices.size();
    }

    public int elementCount() {
        return drawOrder.size();
    }
}
