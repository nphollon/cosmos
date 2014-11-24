package com.aimlessblade.cosmos.graphics.engine;

import com.aimlessblade.cosmos.graphics.Entity;
import com.aimlessblade.cosmos.graphics.camera.Camera;
import lombok.AccessLevel;
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
final class DrawData {
    private final Camera camera;
    private final List<? extends Entity> entities;
    private List<Target> targets;

    FloatBuffer getPerspective() {
        return bufferMatrix(camera.getPerspective());
    }

    List<Target> getTargets() {
        if (targets == null) {
            createTargets();
        }
        return targets;
    }

    FloatBuffer getVertexBuffer() {
        final int dataLength = streamVertexData().mapToInt(List::size).sum();

        final FloatBuffer buffer = BufferUtils.createFloatBuffer(dataLength);
        streamVertexData().forEach(d -> addToBuffer(d, buffer));

        buffer.flip();
        return buffer;
    }

    IntBuffer getElementBuffer() {
        final int elementLength = streamElementData().mapToInt(List::size).sum();

        final IntBuffer buffer = BufferUtils.createIntBuffer(elementLength);

        int entityCount = 0;
        for (Entity entity : entities) {
            final int offset = entityCount;
            entity.getElementData().stream().map(e -> e + offset).forEach(buffer::put);
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

    private Stream<List<Double>> streamVertexData() {
        return entities.stream().map(Entity::getVertexData);
    }

    private Stream<List<Integer>> streamElementData() {
        return entities.stream().map(Entity::getElementData);
    }

    private FloatBuffer bufferMatrix(final SimpleMatrix matrix) {
        final double[] flatMatrixData = matrix.getMatrix().getData();

        final FloatBuffer buffer = BufferUtils.createFloatBuffer(flatMatrixData.length);
        addToBuffer(flatMatrixData, buffer);

        buffer.flip();
        return buffer;
    }

    private void addToBuffer(final double[] data, final FloatBuffer buffer) {
        stream(data).forEach(d -> buffer.put((float) d));
    }

    private void addToBuffer(final List<Double> data, final FloatBuffer buffer) {
        data.stream().map(Double::floatValue).forEach(buffer::put);
    }

    @AllArgsConstructor
    final class Target {
        private final Entity entity;

        @Getter(AccessLevel.PACKAGE)
        private final int elementOffset;

        FloatBuffer getGeoTransform() {
            return bufferMatrix(entity.toMatrix());
        }

        int getElementCount() {
            return entity.getElementData().size();
        }
    }
}
