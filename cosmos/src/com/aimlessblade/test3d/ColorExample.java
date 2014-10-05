package com.aimlessblade.test3d;

import org.lwjgl.LWJGLException;

import java.io.IOException;

import static com.aimlessblade.test3d.GraphicsUtils.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.*;

public class ColorExample extends DisplayFramework {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;

    public static final float[] VERTEX_DATA = new float[]{
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,

            0.0f, 0.5f, 0.0f, 1.0f,
            0.5f, -0.366f, 0.0f, 1.0f,
            -0.5f, -0.366f, 0.0f, 1.0f
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
    }

    public void draw () {
        glUseProgram(program);

        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 48);
        glDrawArrays(GL_TRIANGLES, 0, 3);

        glUseProgram(0);
    }
}
