package com.aimlessblade.test3d;

import org.apache.commons.io.FileUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class MotionExample {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;

    public static final float[] VERTEX_DATA = new float[]{
            0.0f, 0.5f, 0.0f, 1.0f,
            0.5f, -0.366f, 0.0f, 1.0f,
            -0.5f, -0.366f, 0.0f, 1.0f
    };

    public static void main(final String[] argv) {
        MotionExample displayExample = new MotionExample();
        try {
            displayExample.start();
        } catch (IOException | LWJGLException e) {
            e.printStackTrace();
        }
    }

    private void start() throws IOException, LWJGLException {
        
        initializeDisplay();
        int vertexBuffer = initializeVertexBuffer(VERTEX_DATA);

        int program = linkProgram(
                compileShader("motionTest.vert", GL_VERTEX_SHADER),
                compileShader("motionTest.frag", GL_FRAGMENT_SHADER)
        );

        while (!Display.isCloseRequested()) {
            draw(vertexBuffer, program);
            updateVertexBuffer(vertexBuffer, VERTEX_DATA);
        }

        Display.destroy();
    }

    private void draw(final int vertexBuffer, final int program) {
        glClear(GL_COLOR_BUFFER_BIT);

        glUseProgram(program);
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
        glDrawArrays(GL_TRIANGLES, 0, 3);

        glDisableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glUseProgram(0);
        Display.sync(60);
        Display.update();
    }

    private int linkProgram(final Integer... shaders) {
        int program = glCreateProgram();

        for (int shader : shaders) {
            glAttachShader(program, shader);
        }

        glLinkProgram(program);

        int status = glGetProgrami(program, GL_LINK_STATUS);
        if (status == GL_FALSE) {
            int logLength = glGetProgrami(program, GL_INFO_LOG_LENGTH);
            System.out.println("Compile log:");
            System.out.println(glGetProgramInfoLog(program, logLength));
            throw new RuntimeException("Linking failed!");
        }

        for (int shader : shaders) {
            glDetachShader(program, shader);
        }

        return program;
    }

    private void updateVertexBuffer(final int vertexBufferObject, final float[] vertices) {
        FloatBuffer vertexDataBuffer = createVertexDataBuffer(vertices);

        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertexDataBuffer);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private int initializeVertexBuffer(final float[] vertices) {
        FloatBuffer vertexDataBuffer = createVertexDataBuffer(vertices);

        int vertexBufferObject = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);
        glBufferData(GL_ARRAY_BUFFER, vertexDataBuffer, GL_STREAM_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return vertexBufferObject;
    }

    private FloatBuffer createVertexDataBuffer(final float[] vertices) {
        double time = System.currentTimeMillis() * 1e-3;
        double freq = 2 * Math.PI / 5.0;
        double xOffset = 0.5 * Math.cos(freq * time);
        double yOffset = 0.5 * Math.sin(freq * time);

        int dataLength = vertices.length;
        float[] transformedVertices = Arrays.copyOf(vertices, dataLength);

        for (int i = 0; i < dataLength; i += 4) {
            transformedVertices[i] += (float) xOffset;
            transformedVertices[i+1] += (float) yOffset;
        }

        FloatBuffer vertexDataBuffer = BufferUtils.createFloatBuffer(dataLength);
        vertexDataBuffer.put(transformedVertices);
        vertexDataBuffer.flip();
        return vertexDataBuffer;
    }

    private void initializeDisplay() throws LWJGLException {
        PixelFormat pixelFormat = new PixelFormat();
        ContextAttribs contextAtrributes = new ContextAttribs(3, 0)
                .withForwardCompatible(true);

        Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
        Display.create(pixelFormat, contextAtrributes);

        glViewport(0, 0, WIDTH, HEIGHT);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    private int compileShader(final String filename, final int shaderType) throws IOException {
        File shaderFile = new File("/home/nick/IdeaProjects/cosmos/cosmos/shaders/" + filename);
        byte[] shaderSourceBytes = FileUtils.readFileToByteArray(shaderFile);
        ByteBuffer shaderSource = BufferUtils.createByteBuffer(shaderSourceBytes.length);
        shaderSource.put(shaderSourceBytes);
        shaderSource.flip();

        int shader = glCreateShader(shaderType);
        glShaderSource(shader, shaderSource);
        glCompileShader(shader);

        int status = glGetShaderi(shader, GL_COMPILE_STATUS);

        if (status == GL_FALSE) {
            int logLength = glGetShaderi(shader, GL_INFO_LOG_LENGTH);
            System.out.println("Compile log:");
            System.out.println(glGetShaderInfoLog(shader, logLength));
            throw new RuntimeException(filename + " compilation failed!");
        }

        return shader;
    }

    private void exitOnGLError(String errorMessage) {
        int errorValue = GL11.glGetError();

        if (errorValue != GL11.GL_NO_ERROR) {
            String errorString = GLU.gluErrorString(errorValue);
            System.err.println("ERROR - " + errorMessage + ": " + errorString);

            if (Display.isCreated()) Display.destroy();
            System.exit(-1);
        }
    }
}
