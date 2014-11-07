package com.aimlessblade.cosmos.render;

import com.aimlessblade.cosmos.geo.Camera;
import com.aimlessblade.cosmos.geo.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.ejml.simple.SimpleMatrix;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

@RequiredArgsConstructor
public final class DrawData {
    private final Camera camera;
    private final List<? extends Entity> entities;
    private List<Target> targets;

    public FloatBuffer getPerspective() {
        return bufferMatrix(camera.getPerspective());
    }

    public List<Target> getTargets() {
        if (targets == null) {
            createTargets();
        }
        return targets;
    }

    public FloatBuffer getVertexBuffer() {
        final int dataLength = streamVertexData().mapToInt(e -> e.length).sum();

        final FloatBuffer buffer = BufferUtils.createFloatBuffer(dataLength);
        streamVertexData().forEach(d -> addToBuffer(d, buffer));

        buffer.flip();
        return buffer;
    }

    public IntBuffer getElementBuffer() {
        final int elementLength = streamElementData().mapToInt(e -> e.length).sum();

        final IntBuffer buffer = BufferUtils.createIntBuffer(elementLength);

        int entityCount = 0;
        for (Entity entity : entities) {
            final int offset = entityCount;
            stream(entity.getElementData()).map(e -> e + offset).forEach(buffer::put);
            entityCount += entity.getVertexCount();
        }

        buffer.flip();
        return buffer;
    }

    private void createTargets() {
        targets = new ArrayList<>();

        int globalElementCount = 0;
        for (Entity entity : entities) {
            final Target target = new Target(entity, globalElementCount);
            targets.add(target);
            globalElementCount += target.getElementCount();
        }
    }

    private Stream<double[]> streamVertexData() {
        return entities.stream().map(Entity::getVertexData);
    }

    private Stream<int[]> streamElementData() {
        return entities.stream().map(Entity::getElementData);
    }

    private FloatBuffer bufferMatrix(final SimpleMatrix matrix) {
        final double[] flatMatrixData = matrix.getMatrix().data;
        final FloatBuffer buffer = BufferUtils.createFloatBuffer(flatMatrixData.length);
        addToBuffer(flatMatrixData, buffer);
        buffer.flip();
        return buffer;
    }

    private void addToBuffer(final double[] data, final FloatBuffer buffer) {
        stream(data).forEach(d -> buffer.put((float) d));
    }

    @AllArgsConstructor
    public final class Target {
        private final Entity entity;
        @Getter private final int elementOffset;

        public FloatBuffer getGeoTransform() {
            return bufferMatrix(entity.getGeoTransform());
        }

        public int getElementCount() {
            return entity.getElementData().length;
        }
    }
}
