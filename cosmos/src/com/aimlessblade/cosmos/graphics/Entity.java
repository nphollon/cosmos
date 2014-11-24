package com.aimlessblade.cosmos.graphics;

import com.aimlessblade.cosmos.physics.ToMatrix;

import java.util.List;

public interface Entity extends ToMatrix {

    List<Double> getVertexData();

    int getVertexCount();

    int[] getElementData();
}
