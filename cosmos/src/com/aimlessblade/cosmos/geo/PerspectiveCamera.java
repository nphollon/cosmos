package com.aimlessblade.cosmos.geo;

import org.ejml.simple.SimpleMatrix;

public final class PerspectiveCamera implements Camera {
    private final double frustumScale;
    private final double zNear;
    private final double zFar;
    private final double aspectRatio;

    public PerspectiveCamera(final double frustumScale, final double zNear, final double zFar, final double aspectRatio) {
        validateGeometry(frustumScale, zNear, zFar, aspectRatio);

        this.frustumScale = frustumScale;
        this.zNear = zNear;
        this.zFar = zFar;
        this.aspectRatio = aspectRatio;

    }

    public SimpleMatrix getPerspective() {
        SimpleMatrix perspective = new SimpleMatrix(4, 4);
        perspective.set(0, 0, frustumScale / aspectRatio);
        perspective.set(1, 1, frustumScale);
        perspective.set(2, 2, (zNear + zFar) / (zNear - zFar));
        perspective.set(2, 3, 2 * zNear * zFar / (zNear - zFar));
        perspective.set(3, 2, -1);
        return perspective;
    }

    private void validateGeometry(final double frustumScale, final double zNear, final double zFar, final double aspectRatio) {
        if (frustumScale <= 0 || zNear <= 0 || zFar <= zNear || aspectRatio <= 0) {
            throw new ArithmeticException("Invalid perspective parameters: frustumScale = " + frustumScale +
                    ", zNear = " + zNear + ", zFar = " + zFar + ", aspectRatio = " + aspectRatio);
        }
    }
}
