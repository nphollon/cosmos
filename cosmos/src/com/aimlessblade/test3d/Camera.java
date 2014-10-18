package com.aimlessblade.test3d;

import lombok.Getter;
import org.ejml.simple.SimpleMatrix;

public class Camera {
    @Getter private SimpleMatrix perspective;

    public Camera(double frustumScale, double zNear, double zFar, double aspectRatio) {
        perspective = new SimpleMatrix(4, 4);
        perspective.set(0, 0, frustumScale / aspectRatio);
        perspective.set(1, 1, frustumScale);
        perspective.set(2, 2, (zFar + zNear) / (zFar - zNear));
        perspective.set(2, 3, 2 * zFar * zNear / (zFar - zNear));
        perspective.set(3, 2, -1);
    }
}
