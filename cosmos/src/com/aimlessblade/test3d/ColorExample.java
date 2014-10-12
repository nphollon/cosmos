package com.aimlessblade.test3d;

import org.lwjgl.LWJGLException;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Arrays;

import static com.aimlessblade.test3d.Color.*;
import static com.aimlessblade.test3d.GraphicsUtils.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class ColorExample extends DisplayFramework {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final float ASPECT_RATIO = (float) WIDTH / HEIGHT;

    private static final float FRUSTUM_SCALE = 1.0f;
    private static final float Z_NEAR = 1.0f;
    private static final float Z_FAR = 3.0f;
    private static final float[] PERSPECTIVE_MATRIX = getPerspectiveMatrix(FRUSTUM_SCALE, Z_NEAR, Z_FAR, ASPECT_RATIO);
    
    private static final float[] ROTATION_MATRIX = getYawRotationMatrix(15);

    private static final float LEFT = -0.25f;
    private static final float RIGHT = 0.25f;
    private static final float UP = 0.25f;
    private static final float DOWN = -0.25f;
    private static final float FRONT = -1.25f;
    private static final float BACK = -2.75f;

    private static final Position UFL = new Position(LEFT, UP, FRONT);
    private static final Position UFR = new Position(RIGHT, UP, FRONT);
    private static final Position UBL = new Position(LEFT, UP, BACK);
    private static final Position UBR = new Position(RIGHT, UP, BACK);
    private static final Position DFL = new Position(LEFT, DOWN, FRONT);
    private static final Position DFR = new Position(RIGHT, DOWN, FRONT);
    private static final Position DBL = new Position(LEFT, DOWN, BACK);
    private static final Position DBR = new Position(RIGHT, DOWN, BACK);

    private static final float[] VERTEX_DATA = Vertex.asFloatArray(Arrays.asList(
            new Vertex(UFL, BLUE),
            new Vertex(UBL, BLUE),
            new Vertex(UBR, BLUE),
            new Vertex(UBR, BLUE),
            new Vertex(UFR, BLUE),
            new Vertex(UFL, BLUE),

            new Vertex(DFL, GRAY),
            new Vertex(DFR, GRAY),
            new Vertex(DBR, GRAY),
            new Vertex(DBR, GRAY),
            new Vertex(DBL, GRAY),
            new Vertex(DFL, GRAY),

            new Vertex(UFL, GREEN),
            new Vertex(DFL, GREEN),
            new Vertex(DBL, GREEN),
            new Vertex(DBL, GREEN),
            new Vertex(UBL, GREEN),
            new Vertex(UFL, GREEN),

            new Vertex(UFR, YELLOW),
            new Vertex(UBR, YELLOW),
            new Vertex(DBR, YELLOW),
            new Vertex(DBR, YELLOW),
            new Vertex(DFR, YELLOW),
            new Vertex(UFR, YELLOW),

            new Vertex(UFL, RED),
            new Vertex(UFR, RED),
            new Vertex(DFR, RED),
            new Vertex(DFR, RED),
            new Vertex(DFL, RED),
            new Vertex(UFL, RED),

            new Vertex(UBL, CYAN),
            new Vertex(DBL, CYAN),
            new Vertex(DBR, CYAN),
            new Vertex(DBR, CYAN),
            new Vertex(UBR, CYAN),
            new Vertex(UBL, CYAN)
    ));

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
        int vertexArray = glGenVertexArrays();
        glBindVertexArray(vertexArray);

        vertexBuffer = initializeBufferObject(GL_ARRAY_BUFFER, VERTEX_DATA, GL_STREAM_DRAW);

        program = linkProgram(
                compileShader("colorTest.vert", GL_VERTEX_SHADER),
                compileShader("colorTest.frag", GL_FRAGMENT_SHADER)
        );

        int offsetLocation = glGetUniformLocation(program, "offset");
        int perspectiveMatrixLocation = glGetUniformLocation(program, "perspectiveMatrix");
        int rotationMatrixLocation = glGetUniformLocation(program, "rotationMatrix");

        glUseProgram(program);
        glUniform2f(offsetLocation, 0.5f, 0.5f);
        FloatBuffer perspectiveBuffer = createDataBuffer(PERSPECTIVE_MATRIX);
        FloatBuffer rotationBuffer = createDataBuffer(ROTATION_MATRIX);
        glUniformMatrix4(perspectiveMatrixLocation, true, perspectiveBuffer);
        glUniformMatrix4(rotationMatrixLocation, true, rotationBuffer);
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
