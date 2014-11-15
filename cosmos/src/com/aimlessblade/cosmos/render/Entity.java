package com.aimlessblade.cosmos.render;

import org.ejml.simple.SimpleMatrix;

public interface Entity {

    double[] getVertexData();

    int getVertexCount();

    int[] getElementData();

    SimpleMatrix getTransform();
}
