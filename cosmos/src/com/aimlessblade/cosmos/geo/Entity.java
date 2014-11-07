package com.aimlessblade.cosmos.geo;

import org.ejml.simple.SimpleMatrix;

public interface Entity {

    double[] getVertexData();

    int getVertexCount();

    int[] getElementData();

    SimpleMatrix getGeoTransform();
}
