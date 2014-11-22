package com.aimlessblade.cosmos.physics;

import com.aimlessblade.cosmos.render.DrawDataTest;
import org.ejml.simple.SimpleMatrix;

import java.nio.FloatBuffer;

import static java.util.Arrays.stream;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Identity {
    public static final double TOLERANCE = 1e-5;

    public static void assertMatrixEquality(SimpleMatrix actual, SimpleMatrix expected) {
        String error = "Actual:\n" + actual.toString() + "Expected:\n" + expected;
        assertThat(error, actual.isIdentical(expected, TOLERANCE), is(true));
    }

    public static void assertBufferContents(final FloatBuffer buffer, final double[] expectedData) {
        assertThat(buffer.limit(), is(expectedData.length));
        stream(expectedData).forEach(DrawDataTest.checkNextBufferEntry(buffer, TOLERANCE));
    }

    public static void assertOrientationEquality(final Orientation orientation1, final Orientation orientation2) {
        String message = "\nActual:\n" + orientation1 + "\nExpected:\n" + orientation2;
        assertThat(message, orientation1.isIdentical(orientation2, TOLERANCE), is(true));
        assertThat(message, orientation2.isIdentical(orientation1, TOLERANCE), is(true));
    }

    public static void assertOrientationInequality(final Orientation orientation1, final Orientation orientation2) {
        String message = "\nActual:\n" + orientation1 + "\nExpected not to equal:\n" + orientation2;
        assertThat(message, orientation1.isIdentical(orientation2, TOLERANCE), is(false));
        assertThat(message, orientation2.isIdentical(orientation1, TOLERANCE), is(false));
    }
}
