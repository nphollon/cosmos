package com.aimlessblade.cosmos.graphics.vertex;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class VertexFactoryTest {
    @Test
    public void factoryLengthShouldBeLengthOfOnlyAttribute() {
        VertexFactory factory = new VertexFactory(new Attribute("color", 3));

        assertThat(factory.getLength(), is(3));
    }

    @Test
    public void factoryLengthShouldBeSumOfAttributeLengths() {
        VertexFactory factory = new VertexFactory(new Attribute("color", 3), new Attribute("height", 1));

        assertThat(factory.getLength(), is(4));
    }

    @Test
    public void factoryStrideShouldBeLengthTimeSizeOfFloat() {
        VertexFactory factory = new VertexFactory(new Attribute("color", 3), new Attribute("position", 3));

        assertThat(factory.getStride(), is(6 * Float.BYTES));
    }

    @Test
    public void firstFactoryAttributeShouldHaveOffsetZero() {
        VertexFactory factory = new VertexFactory(new Attribute("color", 3));

        List<LoadedAttribute> attributes = factory.getAttributes();

        List<LoadedAttribute> expectedAttributes = Arrays.asList(new LoadedAttribute("color", 3, 0));
        assertThat(attributes, is(expectedAttributes));
    }

    @Test
    public void factoryAttributeOffsetShouldIncreaseBySizeOfPreviousAttribute() {
        VertexFactory factory = new VertexFactory(new Attribute("color", 3), new Attribute("position", 3));

        List<LoadedAttribute> attributes = factory.getAttributes();

        List<LoadedAttribute> expectedAttributes = Arrays.asList(new LoadedAttribute("color", 3, 0),
                new LoadedAttribute("position", 3, 3 * Float.BYTES));
        assertThat(attributes, is(expectedAttributes));
    }

    @Test
    public void factoryWithLengthThreeShouldAcceptThreeVertexComponents() {
        VertexFactory factory = new VertexFactory(new Attribute("color", 3));
        Vertex vertex = factory.build(11, 12, 13);

        assertThat(vertex.data(), is(new double[] { 11, 12, 13 }));
    }

    @Test(expected = VertexDataException.class)
    public void factoryShouldNotAcceptTooManyComponents() {
        new VertexFactory(new Attribute("temperature", 1)).build(5, 5.3);
    }

    @Test(expected = VertexDataException.class)
    public void factoryShouldNotAcceptTooFewComponents() {
        new VertexFactory(new Attribute("smell", 5)).build(5, 5.3, 9, 1);
    }
}
