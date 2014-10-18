package com.aimlessblade.test3d;

import lombok.Getter;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

@Getter
public class World {
    private static final int ATTRIBUTE_SIZE = 3;
    private static final int ATTRIBUTE_COUNT = 2;

    private final List<Range> ranges;
    private final float[] vertexData;
    private final int[] elementData;

    public World(DrawableObject... objects) {
        ranges = new ArrayList<>();

        int globalVertexCount = 0;
        int globalElementCount = 0;

        for (DrawableObject object : objects) {
            ranges.add(new Range(object, globalVertexCount, globalElementCount));
            globalVertexCount += object.vertexCount();
            globalElementCount += object.elementCount();
        }

        vertexData = new float[globalVertexCount * ATTRIBUTE_COUNT * ATTRIBUTE_SIZE];
        elementData = new int[globalElementCount];

        int vertexIndex = 0;
        int elementIndex = 0;

        for (Range range : ranges) {
            DrawableObject object = range.getObject();

            for (Vertex vertex : object.getVertices()) {
                flatConcat(vertex.getColor(), vertexIndex);
                vertexIndex += ATTRIBUTE_SIZE;
                loadAttribute(vertex.getPosition(), vertexIndex);
                vertexIndex += ATTRIBUTE_SIZE;
            }

            for (int objectElement : object.getDrawOrder()) {
                elementData[elementIndex] = objectElement + range.getVertexOffset();
                elementIndex += 1;
            }
        }
    }

    private void loadAttribute(Position position, int startIndex) {
        vertexData[startIndex] = (float) position.getX();
        vertexData[startIndex + 1] = (float) position.getY();
        vertexData[startIndex + 2] = (float) position.getZ();
    }

    private void flatConcat(Color color, int startIndex) {
        vertexData[startIndex] = (float) color.getRed();
        vertexData[startIndex + 1] = (float) color.getGreen();
        vertexData[startIndex + 2] = (float) color.getBlue();
    }

    public IntBuffer getElementBuffer() {
        IntBuffer dataBuffer = BufferUtils.createIntBuffer(elementData.length);
        dataBuffer.put(elementData);
        dataBuffer.flip();
        return dataBuffer;
    }

    public FloatBuffer getVertexBuffer() {
        FloatBuffer dataBuffer = BufferUtils.createFloatBuffer(vertexData.length);
        dataBuffer.put(vertexData);
        dataBuffer.flip();
        return dataBuffer;
    }

    final class Range {
        @Getter private final DrawableObject object;
        @Getter private final int vertexOffset;
        @Getter private final int elementOffset;

        public Range(DrawableObject object, int vertexOffset, int elementOffset) {
            this.object = object;
            this.vertexOffset = vertexOffset;
            this.elementOffset = elementOffset;
        }

        public int getElementCount() {
            return object.elementCount();
        }
    }
}
