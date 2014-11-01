package com.aimlessblade.cosmos.geo;

import org.ejml.simple.SimpleMatrix;

public interface Camera extends Movable {
    SimpleMatrix getPerspective();
}
