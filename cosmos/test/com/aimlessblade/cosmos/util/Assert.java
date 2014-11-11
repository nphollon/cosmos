package com.aimlessblade.cosmos.util;

import org.ejml.simple.SimpleMatrix;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;

import static java.util.Arrays.stream;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

public class Assert {
    public static void assertMatrixEquality(SimpleMatrix actual, SimpleMatrix expected, final double tolerance) {
        String error = "Actual:\n" + actual.toString() + "Expected:\n" + expected;
        assertThat(error, actual.isIdentical(expected, tolerance), is(true));
    }

    public static void assertBufferContents(final FloatBuffer buffer, final double[] expectedData, final double tolerance) {
        assertThat(buffer.limit(), is(expectedData.length));
        stream(expectedData).forEach(checkNextBufferEntry(buffer, tolerance));
    }

    public static void assertBufferContents(final IntBuffer buffer, final int[] expectedData) {
        assertThat(buffer.limit(), is(expectedData.length));
        stream(expectedData).forEach(checkNextBufferEntry(buffer));
    }

    private static DoubleConsumer checkNextBufferEntry(final FloatBuffer buffer, final double tolerance) {
        return d -> assertThat((double) buffer.get(), closeTo(d, tolerance));
    }

    private static IntConsumer checkNextBufferEntry(final IntBuffer buffer) {
        return i -> assertThat(buffer.get(), is(i));
    }
}
