package com.aimlessblade.test3d;

import org.apache.commons.io.FileUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class GraphicsUtils {
    private GraphicsUtils() {}

    public static int linkProgram(final Integer... shaders) {
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

    public static int initializeBufferObject(final int target, final float[] data, final int drawMode) {
        FloatBuffer dataBuffer = createDataBuffer(data);

        int bufferObject = glGenBuffers();
        glBindBuffer(target, bufferObject);
        glBufferData(target, dataBuffer, drawMode);

        glBindBuffer(target, 0);
        return bufferObject;
    }

    public static int initializeBufferObject(final int target, final int[] data, final int drawMode) {
        IntBuffer dataBuffer = createDataBuffer(data);

        int bufferObject = glGenBuffers();
        glBindBuffer(target, bufferObject);
        glBufferData(target, dataBuffer, drawMode);

        glBindBuffer(target, 0);
        return bufferObject;
    }

    public static FloatBuffer createDataBuffer(final float[] data) {
        FloatBuffer dataBuffer = BufferUtils.createFloatBuffer(data.length);
        dataBuffer.put(data);
        dataBuffer.flip();
        return dataBuffer;
    }

    public static IntBuffer createDataBuffer(final int[] data) {
        IntBuffer dataBuffer = BufferUtils.createIntBuffer(data.length);
        dataBuffer.put(data);
        dataBuffer.flip();
        return dataBuffer;
    }

    public static int compileShader(final String filename, final int shaderType) throws IOException {
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

    public static void exitOnGLError(String errorMessage) {
        int errorValue = GL11.glGetError();

        if (errorValue != GL11.GL_NO_ERROR) {
            String errorString = GLU.gluErrorString(errorValue);
            System.err.println("ERROR - " + errorMessage + ": " + errorString);

            if (Display.isCreated()) Display.destroy();
            System.exit(-1);
        }
    }

    public static float[] getPerspectiveMatrix(float frustumScale, float zNear, float zFar, float aspectRatio) {
        // 2D matrix stored in a 1D array (row-major order)
        return new float[] {
            frustumScale/aspectRatio,      0,                 0,                        0,
                          0,          frustumScale,           0,                        0,
                          0,               0,      (zFar+zNear)/(zFar-zNear), 2*zFar*zNear/(zFar-zNear),
                          0,               0,                -1,                        0
        };
    }

    public static float[] getYawRotationMatrix(float yaw) {
        return new float[] {
             cos(yaw), 0, sin(yaw), 0,
                 0,    1,    0,     0,
            -sin(yaw), 0, cos(yaw), 0,
                 0,    0,    0,     1
        };
    }

    private static float cos(float degrees) {
        return (float) Math.cos(Math.toRadians(degrees));
    }

    private static float sin(float degrees) {
        return (float) Math.sin(Math.toRadians(degrees));
    }
}
