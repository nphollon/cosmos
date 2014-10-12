package com.aimlessblade.test3d;

import org.lwjgl.LWJGLException;

import java.io.IOException;
import java.util.Arrays;

import static com.aimlessblade.test3d.Color.*;
import static com.aimlessblade.test3d.GraphicsUtils.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class DepthExample extends DisplayFramework {

    public static final int SCREEN_WIDTH = 500;
    private static final float ASPECT_RATIO = 1.5f;
    private static final float FRUSTUM_SCALE = 1;
    private static final float Z_NEAR = 1;
    private static final float Z_FAR = 3;

    private static final float SHORT = 0.2f;
    private static final float LONG = 0.8f;
    private static final float CENTER = 0.0f;
    private static final float NEAR = -1.25f;
    private static final float FAR = -1.75f;

    private static final Position TOP_POINT = new Position(CENTER, LONG, NEAR);
    private static final Position BOTTOM_POINT = new Position(CENTER, -LONG, NEAR);
    private static final Position TOP_LEFT_BASE = new Position(-SHORT, LONG, FAR);
    private static final Position TOP_RIGHT_BASE = new Position(SHORT, LONG, FAR);
    private static final Position BOTTOM_LEFT_BASE = new Position(-SHORT, -LONG, FAR);
    private static final Position BOTTOM_RIGHT_BASE = new Position(SHORT, -LONG, FAR);

    private static final Position LEFT_POINT = new Position(-LONG, CENTER, NEAR);
    private static final Position RIGHT_POINT = new Position(LONG, CENTER, NEAR);
    private static final Position LEFT_TOP_BASE = new Position(-LONG, SHORT, FAR);
    private static final Position LEFT_BOTTOM_BASE = new Position(-LONG, -SHORT, FAR);
    private static final Position RIGHT_TOP_BASE = new Position(LONG, SHORT, FAR);
    private static final Position RIGHT_BOTTOM_BASE = new Position(LONG, -SHORT, FAR);

    private static final float[] VERTEX_DATA = Vertex.asFloatArray(Arrays.asList(
            // Horizontal prism
            new Vertex(LEFT_POINT, GREEN),
            new Vertex(LEFT_BOTTOM_BASE, GREEN),
            new Vertex(LEFT_TOP_BASE, GREEN),

            new Vertex(RIGHT_POINT, GRAY),
            new Vertex(RIGHT_TOP_BASE, GRAY),
            new Vertex(RIGHT_BOTTOM_BASE, GRAY),

            new Vertex(LEFT_POINT, RED),
            new Vertex(LEFT_TOP_BASE, RED),
            new Vertex(RIGHT_TOP_BASE, RED),
            new Vertex(RIGHT_POINT, RED),
            
            new Vertex(LEFT_POINT, YELLOW),
            new Vertex(RIGHT_POINT, YELLOW),
            new Vertex(RIGHT_BOTTOM_BASE, YELLOW),
            new Vertex(LEFT_BOTTOM_BASE, YELLOW),
            
            new Vertex(LEFT_TOP_BASE, CYAN),
            new Vertex(LEFT_BOTTOM_BASE, CYAN),
            new Vertex(RIGHT_BOTTOM_BASE, CYAN),
            new Vertex(RIGHT_TOP_BASE, CYAN),
            
            // Vertical prism
            new Vertex(TOP_POINT, GREEN),
            new Vertex(TOP_LEFT_BASE, GREEN),
            new Vertex(TOP_RIGHT_BASE, GREEN),

            new Vertex(BOTTOM_POINT, RED),
            new Vertex(BOTTOM_RIGHT_BASE, RED),
            new Vertex(BOTTOM_LEFT_BASE, RED),

            new Vertex(TOP_POINT, BLUE),
            new Vertex(TOP_RIGHT_BASE, BLUE),
            new Vertex(BOTTOM_RIGHT_BASE, BLUE),
            new Vertex(BOTTOM_POINT, BLUE),
            
            new Vertex(TOP_POINT, GRAY),
            new Vertex(BOTTOM_POINT, GRAY),
            new Vertex(BOTTOM_LEFT_BASE, GRAY),
            new Vertex(TOP_LEFT_BASE, GRAY),
            
            new Vertex(TOP_RIGHT_BASE, CYAN),
            new Vertex(TOP_LEFT_BASE, CYAN),
            new Vertex(BOTTOM_LEFT_BASE, CYAN),
            new Vertex(BOTTOM_RIGHT_BASE, CYAN)
    ));

    private static final long VERTEX_DATA_SIZE = VERTEX_DATA.length * Float.BYTES;


    private static final int[] INDEX_DATA = new int[]{
            // Index list is reused for both prisms
            0, 1, 2,

            3, 4, 5,

            6, 7, 8,
            8, 9, 6,

            10, 11, 12,
            12, 13, 10,

            14, 15, 16,
            16, 17, 14
    };

    private int vertexBuffer;
    private int indexBuffer;
    private int program;
    private int offsetUniform;
    private int vertexArray1;
    private int vertexArray2;
    public static final int NUMBER_OF_POINTS = VERTEX_DATA.length / 16;

    public static void main(String[] args) {
        try {
            new DepthExample(SCREEN_WIDTH, (int)(SCREEN_WIDTH / ASPECT_RATIO)).start();
        } catch (IOException|LWJGLException e) {
            e.printStackTrace();
        }
    }

    public DepthExample(int width, int height) {
        super(width, height);
    }

    @Override
    public void initialize() throws IOException {
        initializeProgram();
        initializeVertexBuffer();
        initializeVertexArrays();

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glFrontFace(GL_CW);
    }

    private void initializeVertexArrays() {
        long positionDataOffset1 = 0;
        long colorDataOffset1 = VERTEX_DATA_SIZE / 2;

        vertexArray1 = glGenVertexArrays();
        glBindVertexArray(vertexArray1);

        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, positionDataOffset1);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, colorDataOffset1);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);

        long positionDataOffset2 = VERTEX_DATA_SIZE / 4;
        long colorDataOffset2 = VERTEX_DATA_SIZE * 3 / 4;

        vertexArray2 = glGenVertexArrays();
        glBindVertexArray(vertexArray2);

        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, positionDataOffset2);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, colorDataOffset2);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);

        glBindVertexArray(0);
    }

    private void initializeVertexBuffer() {
        vertexBuffer = initializeBufferObject(GL_ARRAY_BUFFER, VERTEX_DATA, GL_STATIC_DRAW);
        indexBuffer = initializeBufferObject(GL_ELEMENT_ARRAY_BUFFER, INDEX_DATA, GL_STATIC_DRAW);
    }

    private void initializeProgram() throws IOException {
        program = linkProgram(
                compileShader("depthTest.vert", GL_VERTEX_SHADER),
                compileShader("depthTest.frag", GL_FRAGMENT_SHADER)
        );

        offsetUniform = glGetUniformLocation(program, "offset");
        int perspectiveUniform = glGetUniformLocation(program, "perspectiveMatrix");

        glUseProgram(program);
        float[] perspectiveMatrix = getPerspectiveMatrix(FRUSTUM_SCALE, Z_NEAR, Z_FAR, ASPECT_RATIO);
        glUniformMatrix4(perspectiveUniform, true, createDataBuffer(perspectiveMatrix));

        glUseProgram(0);
    }

    @Override
    public void draw() {
        glUseProgram(program);

        glBindVertexArray(vertexArray1);
        glUniform3f(offsetUniform, 0, 0, 0);
        glDrawElements(GL_TRIANGLES, NUMBER_OF_POINTS, GL_UNSIGNED_INT, 0);

        glBindVertexArray(vertexArray2);
        glUniform3f(offsetUniform, 0, 0, -1);
        glDrawElements(GL_TRIANGLES, NUMBER_OF_POINTS, GL_UNSIGNED_INT, 0);

        glBindVertexArray(0);
        glUseProgram(0);
    }
}
