package com.aimlessblade.cosmos.render;

import com.aimlessblade.cosmos.app.Processor;
import com.aimlessblade.cosmos.camera.Camera;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class DrawingProcessor implements Processor {
    private final ShaderProgram program;
    private final DrawData drawData;

    public static Processor build(final File vertexShader, final File fragmentShader, final Camera camera, final List<? extends Entity> entities) throws IOException {
        final ShaderProgram program = ShaderProgram.build(vertexShader, fragmentShader);
        final DrawData drawData = new DrawData(camera, entities);

        final DrawingProcessor processor = new DrawingProcessor(program, drawData);

        processor.loadVertexData();

        return processor;
    }

    @Override
    public void run() {
        program.use();
        program.setPerspective(drawData.getPerspective());

        for (final DrawData.Target target : drawData.getTargets()) {
            program.setGeoTransform(target.getGeoTransform());
            glDrawElements(GL_TRIANGLES, target.getElementCount(), GL_UNSIGNED_INT, Integer.BYTES * target.getElementOffset());
        }

        program.clear();
    }

    private void loadVertexData() {
        glBindVertexArray(glGenVertexArrays());
        initializeVertexBuffer(GL_STREAM_DRAW);
        initializeElementBuffer(GL_STREAM_DRAW);

        configureFaceCulling();
        configureDepthBuffer();
    }

    private void configureDepthBuffer() {
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDepthFunc(GL_LEQUAL);
        glDepthRange(0, 1);
    }

    private void configureFaceCulling() {
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glFrontFace(GL_CW);
    }

    private void initializeVertexBuffer(final int drawMode) {
        final int bufferObject = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, bufferObject);
        glBufferData(GL_ARRAY_BUFFER, drawData.getVertexBuffer(), drawMode);
        program.defineAttributes();
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void initializeElementBuffer(final int drawMode) {
        final int bufferObject = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferObject);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, drawData.getElementBuffer(), drawMode);
    }
}
