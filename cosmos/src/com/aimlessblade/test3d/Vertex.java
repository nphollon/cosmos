package com.aimlessblade.test3d;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public class Vertex {
    public static final List<Attribute> ATTRIBUTES = Arrays.asList(
            new Attribute(0, 3, 0),
            new Attribute(1, 3, Float.BYTES * 3)
    );
    public static final int STRIDE = Float.BYTES * 6;

    private final Position position;
    private final Color color;

    public static List<Attribute> getAttributes() {
        return ATTRIBUTES;
    }

    public static int getStride() {
        return STRIDE;
    }

    @Getter
    @AllArgsConstructor
    public static class Attribute {
        private final int index;
        private final int length;
        private final int offset;
    }
}
