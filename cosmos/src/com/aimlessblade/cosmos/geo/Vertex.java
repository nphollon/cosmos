package com.aimlessblade.cosmos.geo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public final class Vertex {
    private static final int LENGTH = 6;

    private final double x;
    private final double y;
    private final double z;
    private final double r;
    private final double g;
    private final double b;

    public static Vertex build(double x, double y, double z, double r, double g, double b) {
        return new Vertex(x, y, z, r, g, b);
    }

    public List<Double> data() {
        return Arrays.asList(r, g, b, x, y, z);
    }

    public static int getStride() {
        return Float.BYTES * getLength();
    }

    public static int getLength() {
        return LENGTH;
    }

    public static Attribute[] getAttributes() {
        return Attribute.values();
    }

    private Vertex(final double x, final double y, final double z, final double r, final double g, final double b) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Getter
    @AllArgsConstructor
    public static enum Attribute {
        POSITION(0, 3, 0), COLOR(1, 3, 3 * Float.BYTES);

        private final int index;
        private final int length;
        private final int offset;
    }
}
