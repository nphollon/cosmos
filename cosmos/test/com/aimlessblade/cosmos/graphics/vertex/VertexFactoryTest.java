package com.aimlessblade.cosmos.graphics.vertex;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class VertexFactoryTest {
    @Test
    public void factoryStrideShouldBeLengthTimeSizeOfFloat() {
        VertexFactory factory = new VertexFactory(new AttributeInfo("color", 3), new AttributeInfo("position", 3));

        assertThat(factory.getStride(), is(6 * Float.BYTES));
    }

    @Test
    public void firstFactoryAttributeShouldHaveOffsetZero() {
        VertexFactory factory = new VertexFactory(new AttributeInfo("color", 3));

        List<LoadedAttribute> attributes = factory.getAttributes();

        List<LoadedAttribute> expectedAttributes = Arrays.asList(new LoadedAttribute("color", 3, 0));
        assertThat(attributes, is(expectedAttributes));
    }

    @Test
    public void factoryAttributeOffsetShouldIncreaseBySizeOfPreviousAttribute() {
        VertexFactory factory = new VertexFactory(new AttributeInfo("color", 3), new AttributeInfo("position", 3));

        List<LoadedAttribute> attributes = factory.getAttributes();

        List<LoadedAttribute> expectedAttributes = Arrays.asList(new LoadedAttribute("color", 3, 0),
                new LoadedAttribute("position", 3, 3 * Float.BYTES));
        assertThat(attributes, is(expectedAttributes));
    }

    @Test
    public void factoryWithLengthThreeShouldAcceptThreeVertexComponents() {
        VertexFactory factory = new VertexFactory(new AttributeInfo("color", 3));
        final List<Double> vertexData = Arrays.asList(11., 12., 13.);

        Vertex vertex = factory.build(vertexData);

        assertThat(vertex.data(), is(vertexData));
    }

    @Test(expected = VertexDataException.class)
    public void factoryShouldNotAcceptTooManyComponents() {
        final VertexFactory factory = new VertexFactory(new AttributeInfo("temperature", 1));
        factory.build(Arrays.asList(0.1, 0.2));
    }

    @Test(expected = VertexDataException.class)
    public void factoryShouldNotAcceptTooFewComponents() {
        final VertexFactory factory = new VertexFactory(new AttributeInfo("smell", 5));
        factory.build(Arrays.asList(5.3));
    }
}
