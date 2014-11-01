package com.aimlessblade.cosmos.geo;

import org.ejml.simple.SimpleMatrix;

public interface Entity extends Movable {

    double[] getVertexData();

    int getVertexCount();

    int[] getElementData();

    SimpleMatrix getGeoTransform();
}
