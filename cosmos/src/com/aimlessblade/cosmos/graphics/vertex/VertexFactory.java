package com.aimlessblade.cosmos.graphics.vertex;

import java.util.ArrayList;
import java.util.List;

public final class VertexFactory {
    private static final int COMPONENT_SIZE = Float.BYTES;

    private final List<LoadedAttribute> loadedAttributes;
    private final int length;

    public VertexFactory(final AttributeInfo... attributes) {
        loadedAttributes = new ArrayList<>(attributes.length);
        length = loadAttributesAndGetLength(attributes);
    }

    private int loadAttributesAndGetLength(final AttributeInfo[] attributes) {
        int componentCount = 0;

        for (final AttributeInfo attributeInfo : attributes) {
            load(attributeInfo, componentCount);
            componentCount += attributeInfo.getLength();
        }

        return componentCount;
    }

    private void load(final AttributeInfo attributeInfo, final int componentOffset) {
        final int byteOffset = componentOffset * COMPONENT_SIZE;
        final LoadedAttribute loadedAttribute = new LoadedAttribute(attributeInfo.getName(), attributeInfo.getLength(), byteOffset);
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
