package com.aimlessblade.cosmos.graphics.vertex.io;

import com.aimlessblade.cosmos.graphics.Entity;
import org.ejml.simple.SimpleMatrix;

import java.io.Reader;
import java.util.Arrays;
import java.util.List;

public class PLYParser {
    public PLYHeader readHeader(final Reader reader) {
        return null;
    }

    public Entity buildEntityFromBody(final Reader reader, final PLYHeader header) {
        return new Entity() {
            @Override
            public List<Double> getVertexData() {
                return Arrays.asList(1.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, -1.0, -1.0, 0.0, 0.0, 1.0, 1.0);
            }

            @Override
            public int getVertexCount() {
                return 3;
            }

            @Override
            public List<Integer> getElementData() {
                return Arrays.asList(0, 1, 2);
            }

            @Override
            public SimpleMatrix toMatrix() {
                return null;
            }
        };
    }
}
