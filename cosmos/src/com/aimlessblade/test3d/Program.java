package com.aimlessblade.test3d;

import org.ejml.simple.SimpleMatrix;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class Program {
    private final int id;
    private final int perspectiveUniform;
    private final int geoTransformUniform;

    public Program(int id) {
        this.id = id;
        perspectiveUniform = getUniform("perspectiveMatrix");
        geoTransformUniform = getUniform("geoTransform");
    }

    public void setPerspective(SimpleMatrix perspective) {
        setUniformMatrix(perspectiveUniform, perspective);
    }

    public void setGeoTransform(SimpleMatrix geoTransform) {
        setUniformMatrix(geoTransformUniform, geoTransform);
    }

    public void use() {
        glUseProgram(id);
    }

    public void clear() {
        glUseProgram(0);
    }

    private int getUniform(String uniformName) {
        return glGetUniformLocation(id, uniformName);
    }

    private void setUniformMatrix(int uniform, SimpleMatrix matrix) {
        glUniformMatrix4(uniform, true, createBuffer(matrix));
    }

    private FloatBuffer createBuffer(SimpleMatrix matrix) {
        double[] data = matrix.getMatrix().getData();
        FloatBuffer dataBuffer = BufferUtils.createFloatBuffer(data.length);

        for (double datum : data) {
            dataBuffer.put((float) datum);
        }

        dataBuffer.flip();
        return dataBuffer;
    }
}
