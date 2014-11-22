package com.aimlessblade.cosmos.render;

import com.aimlessblade.cosmos.physics.ToMatrix;
import org.ejml.simple.SimpleMatrix;

interface Entity extends ToMatrix {

    double[] getVertexData();

    int getVertexCount();

    int[] getElementData();
}
