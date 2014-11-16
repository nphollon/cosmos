package com.aimlessblade.cosmos.render;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class VertexTest {
    @Test
    public void shouldHaveTwoAttributes() {
        final Vertex.Attribute[] attributes = Vertex.getAttributes();
        assertThat(attributes.length, is(2));
        assertThat(attributes[0], is(Vertex.Attribute.POSITION));
        assertThat(attributes[1], is(Vertex.Attribute.COLOR));
    }

    @Test
    public void shouldHavePositionAttribute() {
        final Vertex.Attribute position = Vertex.Attribute.POSITION;
        assertThat(position.getName(), is("position"));
        assertThat(position.getLength(), is(3));
        assertThat(position.getOffset(), is(0));
    }

    @Test
    public void shouldHaveColorAttribute() {
        final Vertex.Attribute color = Vertex.Attribute.COLOR;
        assertThat(color.getName(), is("color"));
        assertThat(color.getLength(), is(3));
        assertThat(color.getOffset(), is(3 * Float.BYTES));
    }

    @Test
    public void shouldHaveStrideOfSixTimesSizeOfFloat() {
        assertThat(Vertex.getStride(), is(6 * Float.BYTES));
    }

    @Test
    public void shouldHaveLengthOfSix() {
        assertThat(Vertex.getLength(), is(6));
    }
}