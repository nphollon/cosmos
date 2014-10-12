package com.aimlessblade.test3d;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang.ArrayUtils;

import java.util.List;

@Getter
@AllArgsConstructor
public class Vertex {
    private final Position position;
    private final Color color;

    public static float[] asFloatArray(final List<Vertex> vertices) {
        int capacity = 4*vertices.size();
        int index = 0;

        float[] positions = new float[capacity];
        float[] colors = new float[capacity];

        for (Vertex vertex : vertices) {
            colors[index] = vertex.color.getRed();
            colors[index+1] = vertex.color.getGreen();
            colors[index+2] = vertex.color.getBlue();
            colors[index+3] = 1.0f;
            
            positions[index] = vertex.position.getX();
            positions[index+1] = vertex.position.getY();
            positions[index+2] = vertex.position.getZ();
            positions[index+3] = 1.0f;

            index += 4;
        }

        return ArrayUtils.addAll(colors, positions);
    }
}
