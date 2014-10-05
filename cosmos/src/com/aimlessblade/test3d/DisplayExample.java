package com.aimlessblade.test3d;

import org.apache.commons.io.FileUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class DisplayExample {

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

    public static void main(String[] argv) {
        DisplayExample displayExample = new DisplayExample();
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
                compileShader("screen.vert", GL_VERTEX_SHADER),
                compileShader("screen.frag", GL_FRAGMENT_SHADER)
        );

        while (!Display.isCloseRequested()) {
            draw(vertexBuffer, program);
        }

        Display.destroy();
    }

    private void draw(int vertexBuffer, int program) {
        glClear(GL_COLOR_BUFFER_BIT);
        glUseProgram(program);

        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 48);
        glDrawArrays(GL_TRIANGLES, 0, 3);

        glUseProgram(0);
        Display.update();
    }

    private int linkProgram(Integer... shaders) {
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

    private int initializeVertexBuffer(float[] vertices) {
        FloatBuffer vertexDataBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexDataBuffer.put(vertices);
        vertexDataBuffer.flip();

        int vertexBufferObject = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);
        glBufferData(GL_ARRAY_BUFFER, vertexDataBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return vertexBufferObject;
    }

    private void initializeDisplay() throws LWJGLException {
        PixelFormat pixelFormat = new PixelFormat();
        ContextAttribs contextAtrributes = new ContextAttribs(3, 0)
                .withForwardCompatible(true);

        Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
        Display.create(pixelFormat, contextAtrributes);

        glViewport(0, 0, WIDTH, HEIGHT);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        System.out.println("OpenGL version: " + glGetString(GL_VERSION));
    }

    private int compileShader(String filename, int shaderType) throws IOException {
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
}
