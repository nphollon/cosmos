package com.aimlessblade.cosmos.graphics.vertex;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class VertexTypeTest {
    @Test
    public void factoryStrideShouldBeLengthTimeSizeOfFloat() {
        VertexType vertexType = new VertexType(new AttributeType("color", 3), new AttributeType("position", 3));

        assertThat(vertexType.getStride(), is(6 * Float.BYTES));
    }

    @Test
    public void firstFactoryAttributeShouldHaveOffsetZero() {
        VertexType vertexType = new VertexType(new AttributeType("color", 3));

        List<LoadedAttribute> attributes = vertexType.getAttributes();

        List<LoadedAttribute> expectedAttributes = Arrays.asList(new LoadedAttribute("color", 3, 0));
        assertThat(attributes, is(expectedAttributes));
    }

    @Test
    public void factoryAttributeOffsetShouldIncreaseBySizeOfPreviousAttribute() {
        VertexType vertexType = new VertexType(new AttributeType("color", 3), new AttributeType("position", 3));

        List<LoadedAttribute> attributes = vertexType.getAttributes();

        List<LoadedAttribute> expectedAttributes = Arrays.asList(new LoadedAttribute("color", 3, 0),
                new LoadedAttribute("position", 3, 3 * Float.BYTES));
        assertThat(attributes, is(expectedAttributes));
    }

    @Test
    public void factoryWithLengthThreeShouldAcceptThreeVertexComponents() throws Exception {
        VertexType vertexType = new VertexType(new AttributeType("color", 3));
        final List<Double> vertexData = Arrays.asList(11., 12., 13.);

        Vertex vertex = vertexType.build(vertexData);

        assertThat(vertex.data(), is(vertexData));
    }

    @Test(expected = VertexDataException.class)
    public void factoryShouldNotAcceptTooManyComponents() throws Exception {
        final VertexType vertexType = new VertexType(new AttributeType("temperature", 1));
        vertexType.build(Arrays.asList(0.1, 0.2));
    }

    @Test(expected = VertexDataException.class)
    public void factoryShouldNotAcceptTooFewComponents() throws Exception {
        final VertexType vertexType = new VertexType(new AttributeType("smell", 5));
        vertexType.build(Arrays.asList(5.3));
    }

    @Test
    public void constructedVertexShouldHaveReferenceToVertexInfo() throws Exception {
        final VertexType vertexType = new VertexType(new AttributeType("moxie", 1));
        Vertex vertex = vertexType.build(Arrays.asList(1.2));

        assertThat(vertex.getType(), is(vertexType));
    }
}
