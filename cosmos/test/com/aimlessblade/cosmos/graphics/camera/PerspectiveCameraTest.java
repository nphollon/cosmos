package com.aimlessblade.cosmos.graphics.camera;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PerspectiveCameraTest {
    @Test(expected = ArithmeticException.class)
    public void shouldThrowExceptionIfFrustumScaleZero() {
        new PerspectiveCamera(0, 1, 2, 1);
    }

    @Test(expected = ArithmeticException.class)
    public void shouldThrowExceptionIfFrustumScaleNegative() {
        new PerspectiveCamera(-0.1, 1, 2, 1);
    }

    @Test(expected = ArithmeticException.class)
    public void shouldThrowExceptionIfZNearZero() {
        new PerspectiveCamera(1, 0, 2, 1);
    }

    @Test(expected = ArithmeticException.class)
    public void shouldThrowExceptionIfZNearNegative() {
        new PerspectiveCamera(1, -0.1, 2, 1);
    }

    @Test(expected = ArithmeticException.class)
    public void shouldThrowExceptionIfZFarEqualsZNear() {
        new PerspectiveCamera(1, 1, 1, 1);
    }

    @Test(expected = ArithmeticException.class)
    public void shouldThrowExceptionIfZFarLessThanZNear() {
        new PerspectiveCamera(1, 1, 0.9, 1);
    }

    @Test(expected = ArithmeticException.class)
    public void shouldThrowExceptionIfAspectRatioZero() {
        new PerspectiveCamera(1, 1, 2, 0);
    }

    @Test(expected = ArithmeticException.class)
    public void shouldThrowExceptionIfAspectRatioNegative() {
        new PerspectiveCamera(1, 1, 2, -0.1);
    }

    @Test
    public void shouldComputePerspectiveMatrixFromConstructorArgs() {
        final Camera camera = new PerspectiveCamera(1, 1, 2, 1);
        assertPerspective(camera, perspectiveData(1, 1, -3, -4));
    }

    @Test
    public void shouldCombineZNearAndZFarForZHorizontalComponents() {
        final Camera camera = new PerspectiveCamera(1, 3, 5, 1);
        assertPerspective(camera, perspectiveData(1, 1, -4, -15));
    }

    @Test
    public void shouldSetYDiagonalComponentToFrustumScale() {
        final Camera camera = new PerspectiveCamera(2, 1, 2, 1);
        assertPerspective(camera, perspectiveData(2, 2, -3, -4));
    }

    @Test
    public void shouldSetXDiagonalComponentToFrustumScaleOverAspectRatio() {
        final Camera camera = new PerspectiveCamera(3, 1, 2, 1.5);
        assertPerspective(camera, perspectiveData(2, 3, -3, -4));
    }

    private void assertPerspective(final Camera camera, final double[] expectedPerspective) {
        assertThat(camera.getPerspective().getMatrix().data, is(expectedPerspective));
    }

    private double[] perspectiveData(final int xx, final int yy, final int zz, final int zw) {
        return new double[] {
                    xx, 0, 0, 0,
                    0, yy, 0, 0,
                    0, 0, zz, zw,
                    0, 0, -1, 0
            };
    }
}