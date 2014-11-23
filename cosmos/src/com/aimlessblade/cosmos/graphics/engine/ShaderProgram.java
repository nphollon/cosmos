package com.aimlessblade.cosmos.graphics.engine;

import com.aimlessblade.cosmos.graphics.vertex.LoadedAttribute;
import com.aimlessblade.cosmos.graphics.vertex.VertexFactory;
import org.apache.commons.io.FileUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.*;

final class ShaderProgram {
    private final int id;
    private final int perspectiveUniform;
    private final int geoTransformUniform;

    static ShaderProgram build(final File vertexShader, final File fragmentShader) throws IOException {
        final int id = linkProgram(
                compileShader(vertexShader, GL20.GL_VERTEX_SHADER),
                compileShader(fragmentShader, GL20.GL_FRAGMENT_SHADER)
        );
        return new ShaderProgram(id);
    }

    void defineAttributes(final VertexFactory vertexFactory) {
        for (final LoadedAttribute attribute: vertexFactory.getAttributes()) {
            final String name = attribute.getName();
            final int length = attribute.getLength();
            final int offset = attribute.getOffset();
            final int index = glGetAttribLocation(id, name);

            glEnableVertexAttribArray(index);
            glVertexAttribPointer(index, length, GL_FLOAT, false, vertexFactory.getStride(), offset);
        }
    }

    void setPerspective(final FloatBuffer perspective) {
        setUniformMatrix(perspectiveUniform, perspective);
    }

    void setGeoTransform(final FloatBuffer geoTransform) {
        setUniformMatrix(geoTransformUniform, geoTransform);
    }

    void use() {
        glUseProgram(id);
    }

    void clear() {
        glUseProgram(0);
    }

    private static int linkProgram(final Integer... shaders) {
        final int programId = glCreateProgram();

        forEach(shaders, s -> glAttachShader(programId, s));

        glLinkProgram(programId);

        final int status = glGetProgrami(programId, GL_LINK_STATUS);
        if (status == GL_FALSE) {
            final int logLength = glGetProgrami(programId, GL_INFO_LOG_LENGTH);
            System.out.println("Compile log:");
            System.out.println(glGetProgramInfoLog(programId, logLength));
            throw new RuntimeException("Linking failed!");
        }

        forEach(shaders, s -> glDetachShader(programId, s));

        return programId;
    }

    private static void forEach(final Integer[] shaders, final Consumer<Integer> action) {
        Arrays.stream(shaders).forEach(action);
    }

    private static int compileShader(final File shaderFile, final int shaderType) throws IOException {
        final byte[] shaderSourceBytes = FileUtils.readFileToByteArray(shaderFile);
        final ByteBuffer shaderSource = BufferUtils.createByteBuffer(shaderSourceBytes.length);
        shaderSource.put(shaderSourceBytes);
        shaderSource.flip();

        final int shaderId = glCreateShader(shaderType);
        glShaderSource(shaderId, shaderSource);
        glCompileShader(shaderId);

        final int status = glGetShaderi(shaderId, GL_COMPILE_STATUS);

        if (status == GL_FALSE) {
            final int logLength = glGetShaderi(shaderId, GL_INFO_LOG_LENGTH);
            System.out.println("Compile log:");
            System.out.println(glGetShaderInfoLog(shaderId, logLength));
            throw new RuntimeException(shaderFile.getName() + " compilation failed!");
        }

        return shaderId;
    }

    private ShaderProgram(final int id) {
        this.id = id;
        perspectiveUniform = getUniform("perspectiveMatrix");
        geoTransformUniform = getUniform("geoTransform");
    }

    private int getUniform(final String uniformName) {
        return glGetUniformLocation(id, uniformName);
    }

    private void setUniformMatrix(final int uniform, final FloatBuffer matrixData) {
        glUniformMatrix4(uniform, true, matrixData);
    }

}
