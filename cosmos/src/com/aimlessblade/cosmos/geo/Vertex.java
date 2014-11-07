package com.aimlessblade.cosmos.geo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class Vertex {
    private static final int STRIDE = 6;
    private final double x;
    private final double y;
    private final double z;
    private final double r;
    private final double g;
    private final double b;

    private Vertex(final double x, final double y, final double z, final double r, final double g, final double b) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public List<Double> data() {
        return Arrays.asList(x, y, z, r, g, b);
    }

    public static int getStride() {
        return STRIDE;
    }

    public static Attribute[] getAttributes() {
        return new Attribute[0];
    }

    public static Vertex build(double x, double y, double z, double r, double g, double b) {
        return new Vertex(x, y, z, r, g, b);
    }

    @Getter
    @AllArgsConstructor
    public class Attribute {
        private int index;
        private int length;
        private int offset;
    }
}
