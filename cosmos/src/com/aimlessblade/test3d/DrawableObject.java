package com.aimlessblade.test3d;

import lombok.Getter;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class DrawableObject {
    @Getter private final List<Integer> drawOrder;
    @Getter private final List<Vertex> vertices;
    @Getter private SimpleMatrix geoTransform;

    public DrawableObject(List<Vertex> vertices, List<Integer> drawOrder) {
        this.vertices = new ArrayList<>(vertices);
        this.drawOrder = new ArrayList<>(drawOrder);

        geoTransform = SimpleMatrix.identity(4);
    }

    public void nudge(double x, double y, double z) {
        SimpleMatrix translationMatrix = new SimpleMatrix(4, 4, true,
                1, 0, 0, x,
                0, 1, 0, y,
                0, 0, 1, z,
                0, 0, 0, 1);
        geoTransform = geoTransform.mult(translationMatrix);
    }

    public int vertexCount() {
        return vertices.size();
    }

    public int elementCount() {
        return drawOrder.size();
    }

    public void spin(double pitch, double yaw, double roll) {

        double sinPitch = sin(Math.toRadians(pitch));
        double sinYaw = sin(Math.toRadians(yaw));
        double sinRoll = sin(Math.toRadians(roll));

        double cosPitch = cos(Math.toRadians(pitch));
        double cosYaw = cos(Math.toRadians(yaw));
        double cosRoll = cos(Math.toRadians(roll));

        SimpleMatrix rotationMatrix = new SimpleMatrix(4, 4, true,
                cosRoll*cosYaw, cosRoll*sinYaw*sinPitch - sinRoll*cosPitch, sinRoll*sinPitch + cosRoll*sinYaw*cosPitch, 0,
                sinRoll*cosYaw, sinRoll*sinYaw*sinPitch + cosRoll*cosPitch, -cosRoll*sinPitch + sinRoll*sinYaw*cosPitch, 0,
                -sinYaw, cosYaw*sinPitch, cosYaw*cosPitch, 0,
                0, 0, 0, 1);

        geoTransform = geoTransform.mult(rotationMatrix);
    }
}
