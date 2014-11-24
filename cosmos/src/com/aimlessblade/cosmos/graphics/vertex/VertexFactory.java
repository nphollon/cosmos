package com.aimlessblade.cosmos.graphics.vertex;

import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
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
        return new FactoryVertex(Arrays.asList(ArrayUtils.toObject(components)));
    }

    private void validateComponentNumber(final int componentCount) {
        if (componentCount != length) {
            throw new VertexDataException("Expected " + length + " vertex components, but received " + componentCount);
        }
    }

    public int getStride() {
        return COMPONENT_SIZE * length;
    }

    public List<LoadedAttribute> getAttributes() {
        return loadedAttributes;
    }

    private static class FactoryVertex implements Vertex {
        private final List<Double> components;

        public FactoryVertex(final List<Double> components) {
            this.components = components;
        }

        @Override
        public List<Double> data() {
            return components;
        }
    }
}
