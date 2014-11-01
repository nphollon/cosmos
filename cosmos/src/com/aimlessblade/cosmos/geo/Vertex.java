package com.aimlessblade.cosmos.geo;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Vertex {
    private static int stride;

    public static int getStride() {
        return stride;
    }

    public static Attribute[] getAttributes() {
        return new Attribute[0];
    }

    @Getter
    @AllArgsConstructor
    public class Attribute {
        private int index;
        private int length;
        private int offset;
    }
}
