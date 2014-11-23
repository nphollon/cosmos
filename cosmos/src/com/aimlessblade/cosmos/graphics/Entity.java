package com.aimlessblade.cosmos.graphics;

import com.aimlessblade.cosmos.physics.ToMatrix;

public interface Entity extends ToMatrix {

    double[] getVertexData();

    int getVertexCount();

    int[] getElementData();
}
