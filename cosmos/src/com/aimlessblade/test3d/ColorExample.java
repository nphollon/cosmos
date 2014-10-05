package com.aimlessblade.test3d;

import org.lwjgl.LWJGLException;

import java.io.IOException;
import java.nio.FloatBuffer;

import static com.aimlessblade.test3d.GraphicsUtils.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.*;

public class ColorExample extends DisplayFramework {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;

    public static final float FRUSTUM_SCALE = 1.0f;
    public static final float Z_NEAR = 1.0f;
    public static final float Z_FAR = 3.0f;
    public static final float[] PERSPECTIVE_MATRIX = getPerspectiveMatrix(FRUSTUM_SCALE, Z_NEAR, Z_FAR);

    public static final float[] VERTEX_DATA = new float[]{
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,

            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,

            0.8f, 0.8f, 0.8f, 1.0f,
            0.8f, 0.8f, 0.8f, 1.0f,
            0.8f, 0.8f, 0.8f, 1.0f,

            0.8f, 0.8f, 0.8f, 1.0f,
            0.8f, 0.8f, 0.8f, 1.0f,
            0.8f, 0.8f, 0.8f, 1.0f,

            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,

            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,

            0.5f, 0.5f, 0.0f, 1.0f,
            0.5f, 0.5f, 0.0f, 1.0f,
            0.5f, 0.5f, 0.0f, 1.0f,

            0.5f, 0.5f, 0.0f, 1.0f,
            0.5f, 0.5f, 0.0f, 1.0f,
            0.5f, 0.5f, 0.0f, 1.0f,

            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,

            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,

            0.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f,

            0.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f,


            0.25f,  0.25f, -1.25f, 1.0f,
            0.25f, -0.25f, -1.25f, 1.0f,
            -0.25f,  0.25f, -1.25f, 1.0f,

            0.25f, -0.25f, -1.25f, 1.0f,
            -0.25f, -0.25f, -1.25f, 1.0f,
            -0.25f,  0.25f, -1.25f, 1.0f,

            0.25f,  0.25f, -2.75f, 1.0f,
            -0.25f,  0.25f, -2.75f, 1.0f,
            0.25f, -0.25f, -2.75f, 1.0f,

            0.25f, -0.25f, -2.75f, 1.0f,
            -0.25f,  0.25f, -2.75f, 1.0f,
            -0.25f, -0.25f, -2.75f, 1.0f,

            -0.25f,  0.25f, -1.25f, 1.0f,
            -0.25f, -0.25f, -1.25f, 1.0f,
            -0.25f, -0.25f, -2.75f, 1.0f,

            -0.25f,  0.25f, -1.25f, 1.0f,
            -0.25f, -0.25f, -2.75f, 1.0f,
            -0.25f,  0.25f, -2.75f, 1.0f,

            0.25f,  0.25f, -1.25f, 1.0f,
            0.25f, -0.25f, -2.75f, 1.0f,
            0.25f, -0.25f, -1.25f, 1.0f,

            0.25f,  0.25f, -1.25f, 1.0f,
            0.25f,  0.25f, -2.75f, 1.0f,
            0.25f, -0.25f, -2.75f, 1.0f,

            0.25f,  0.25f, -2.75f, 1.0f,
            0.25f,  0.25f, -1.25f, 1.0f,
            -0.25f,  0.25f, -1.25f, 1.0f,

            0.25f,  0.25f, -2.75f, 1.0f,
            -0.25f,  0.25f, -1.25f, 1.0f,
            -0.25f,  0.25f, -2.75f, 1.0f,

            0.25f, -0.25f, -2.75f, 1.0f,
            -0.25f, -0.25f, -1.25f, 1.0f,
            0.25f, -0.25f, -1.25f, 1.0f,

            0.25f, -0.25f, -2.75f, 1.0f,
            -0.25f, -0.25f, -2.75f, 1.0f,
            -0.25f, -0.25f, -1.25f, 1.0f
    };

    private int vertexBuffer;
    private int program;

    public ColorExample(int width, int height) {
        super(width, height);
    }

    public static void main(String[] argv) {
        ColorExample colorExample = new ColorExample(WIDTH, HEIGHT);
        try {
            colorExample.start();
        } catch (IOException | LWJGLException e) {
            e.printStackTrace();
        }
    }

    public void initialize() throws IOException {
        vertexBuffer = initializeVertexBuffer(VERTEX_DATA);

        program = linkProgram(
                compileShader("colorTest.vert", GL_VERTEX_SHADER),
                compileShader("colorTest.frag", GL_FRAGMENT_SHADER)
        );

        int offsetLocation = glGetUniformLocation(program, "offset");
        int perspectiveMatrixLocation = glGetUniformLocation(program, "perspectiveMatrix");

        glUseProgram(program);
        glUniform2f(offsetLocation, 0.5f, 0.5f);
        FloatBuffer perspectiveBuffer = createDataBuffer(PERSPECTIVE_MATRIX);
        glUniformMatrix4(perspectiveMatrixLocation, false, perspectiveBuffer);
        glUseProgram(0);

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glFrontFace(GL_CW);
    }

    public void draw () {
        int colorBufferOffset = VERTEX_DATA.length * Float.BYTES / 2;
        int numberOfPoints = VERTEX_DATA.length / 8;

        glUseProgram(program);

        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, colorBufferOffset);
        glDrawArrays(GL_TRIANGLES, 0, numberOfPoints);

        glUseProgram(0);
    }
}
