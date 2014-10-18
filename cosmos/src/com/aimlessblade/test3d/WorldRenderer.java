package com.aimlessblade.test3d;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class WorldRenderer {

    private final World world;
    private final Program program;
    private final Camera camera;

    public WorldRenderer(World world, Program program, Camera camera) {
        this.world = world;
        this.program = program;
        this.camera = camera;

        loadVertexData();
    }

    public void draw() {
        program.use();

        for (World.Range target : world.getRanges()) {
            program.setGeoTransform(target.getObject().getGeoTransform());
            glDrawElements(GL_TRIANGLES, target.getElementCount(), GL_UNSIGNED_INT, Integer.BYTES * target.getElementOffset());
        }

        program.clear();
    }

    private void loadVertexData() {
        program.use();
        program.setPerspective(camera.getPerspective());
        program.clear();

        glBindVertexArray(glGenVertexArrays());
        initializeVertexBuffer(GL_STREAM_DRAW);
        initializeElementBuffer(GL_STREAM_DRAW);

        configureFaceCulling();
        configureDepthBuffer();
    }

    private void configureDepthBuffer() {
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDepthFunc(GL_GEQUAL);
        glDepthRange(0, 1);
    }

    private void configureFaceCulling() {
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glFrontFace(GL_CW);
    }

    private void initializeVertexBuffer(final int drawMode) {
        int bufferObject = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, bufferObject);
        glBufferData(GL_ARRAY_BUFFER, world.getVertexBuffer(), drawMode);

        for (Vertex.Attribute attribute: Vertex.getAttributes()) {
            int index = attribute.getIndex();
            int length = attribute.getLength();
            int offset = attribute.getOffset();
            glEnableVertexAttribArray(index);
            glVertexAttribPointer(index, length, GL_FLOAT, false, Vertex.getStride(), offset);
        }

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void initializeElementBuffer(final int drawMode) {
        int bufferObject = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferObject);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, world.getElementBuffer(), drawMode);
    }
}
