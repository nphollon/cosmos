package com.aimlessblade.cosmos.graphics;

import com.aimlessblade.cosmos.graphics.vertex.VertexType;
import com.aimlessblade.cosmos.physics.ToMatrix;

import java.util.List;

public interface Entity extends ToMatrix {

    List<Double> getVertexData();

    int getVertexCount();

    List<Integer> getElementData();

    VertexType getVertexType();
}
