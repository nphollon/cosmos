package com.aimlessblade.test3d;

import org.lwjgl.LWJGLException;

import java.io.IOException;

import static com.aimlessblade.test3d.GraphicsUtils.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class MotionExample extends DisplayFramework {

    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final long START_TIME = System.currentTimeMillis();
    private static final float PERIOD = 5.0f;
    private static final float COLOR_PERIOD = 1.7f;

    private static final float[] VERTEX_DATA = new float[]{
            0.0f, 0.5f, 0.0f, 1.0f,
            0.5f, -0.366f, 0.0f, 1.0f,
            -0.5f, -0.366f, 0.0f, 1.0f
    };

    private int vertexBuffer;
    private int timeLocation;
    private int program;

    public MotionExample(int width, int height) {
        super(width, height);
    }

    public static void main(final String[] argv) {
        MotionExample displayExample = new MotionExample(WIDTH, HEIGHT);
        try {
            displayExample.start();
        } catch (IOException | LWJGLException e) {
            e.printStackTrace();
        }
    }

    public void initialize() throws IOException {
        vertexBuffer = initializeBufferObject(GL_ARRAY_BUFFER, VERTEX_DATA, GL_STREAM_DRAW);

        program = linkProgram(
                compileShader("motionTest.vert", GL_VERTEX_SHADER),
                compileShader("motionTest.frag", GL_FRAGMENT_SHADER)
        );

        int periodLocation = glGetUniformLocation(program, "period");
        int colorPeriodLocation = glGetUniformLocation(program, "colorPeriod");
        timeLocation = glGetUniformLocation(program, "time");

        glUseProgram(program);
        glUniform1f(periodLocation, PERIOD);
        glUniform1f(colorPeriodLocation, COLOR_PERIOD);
        glUseProgram(0);

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void draw() {
        glUseProgram(program);

        glUniform1f(timeLocation, getElapsedTime());

        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
        glDrawArrays(GL_TRIANGLES, 0, 3);

        glDisableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glUseProgram(0);
    }

    private float getElapsedTime() {
        return (System.currentTimeMillis() - START_TIME) * 0.001f;
    }
}
