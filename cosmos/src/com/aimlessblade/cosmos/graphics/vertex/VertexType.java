package com.aimlessblade.cosmos.graphics.vertex;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public final class VertexType {
    private static final int COMPONENT_SIZE = Float.BYTES;

    private final List<LoadedAttribute> loadedAttributes;
    private final int length;

    public VertexType(final AttributeType... attributes) {
        loadedAttributes = new ArrayList<>(attributes.length);
        length = loadAttributesAndGetLength(attributes);
    }

    private int loadAttributesAndGetLength(final AttributeType[] attributes) {
        int componentCount = 0;

        for (final AttributeType attributeType : attributes) {
            load(attributeType, componentCount);
            componentCount += attributeType.getLength();
        }

        return componentCount;
    }

    private void load(final AttributeType attributeType, final int componentOffset) {
        final int byteOffset = componentOffset * COMPONENT_SIZE;
        final LoadedAttribute loadedAttribute = new LoadedAttribute(attributeType.getName(), attributeType.getLength(), byteOffset);
        loadedAttributes.add(loadedAttribute);
    }

    public Vertex build(final List<Double> components) {
        validateComponentNumber(components.size());
        return new FactoryVertex(components);
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

    private class FactoryVertex implements Vertex {
        private final List<Double> components;

        public FactoryVertex(final List<Double> components) {
            this.components = components;
        }

        @Override
        public List<Double> data() {
            return components;
        }

        @Override
        public VertexType getType() {
            return VertexType.this;
        }
    }
}
