package com.aimlessblade.cosmos.graphics.vertex;

import java.util.ArrayList;
import java.util.List;

public final class VertexFactory {
    private static final int COMPONENT_SIZE = Float.BYTES;

    private final List<LoadedAttribute> loadedAttributes;
    private final int length;

    public VertexFactory(final Attribute... attributes) {
        loadedAttributes = new ArrayList<>(attributes.length);
        length = loadAttributesAndGetLength(attributes);
    }

    private int loadAttributesAndGetLength(final Attribute[] attributes) {
        int totalAttributeLength = 0;
        for (final Attribute a : attributes) {
            final int offset = totalAttributeLength * COMPONENT_SIZE;
            loadedAttributes.add(new LoadedAttribute(a.getName(), a.getLength(), offset));
            totalAttributeLength += a.getLength();
        }
        return totalAttributeLength;
    }

    public Vertex build(final double... components) {
        validateComponentNumber(components.length);
        return new FactoryVertex(components);
    }

    private void validateComponentNumber(final int componentCount) {
        if (componentCount != length) {
            throw new VertexDataException("Expected " + length + " vertex components, but received " + componentCount);
        }
    }

    public int getLength() {
        return length;
    }

    public int getStride() {
        return COMPONENT_SIZE * length;
    }

    public List<LoadedAttribute> getAttributes() {
        return loadedAttributes;
    }

    private static class FactoryVertex implements Vertex {
        private final double[] components;

        public FactoryVertex(final double... components) {
            this.components = components;
        }

        @Override
        public double[] data() {
            return components;
        }
    }
}
