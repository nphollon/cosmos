package com.aimlessblade.cosmos.physics;

import org.ejml.simple.SimpleMatrix;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public final class Identity {
    private static final double TOLERANCE = 1e-5;

    public static void assertMatrixEquality(SimpleMatrix actual, SimpleMatrix expected) {
        String error = "Actual:\n" + actual + "Expected:\n" + expected;
        assertThat(error, actual.isIdentical(expected, TOLERANCE), is(true));
    }

    public static void assertMatrixEquality(ToMatrix actual, ToMatrix expected) {
        assertMatrixEquality(actual.toMatrix(), expected.toMatrix());
    }

    private Identity() {}
}
