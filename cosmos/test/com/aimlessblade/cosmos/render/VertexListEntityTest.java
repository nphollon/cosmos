package com.aimlessblade.cosmos.render;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class VertexListEntityTest {
    private static final int[] DRAW_ORDER = new int[]{12, 13, 14, 15, 16};
    private static final List<Vertex> VERTICES = Arrays.asList(
            Vertex.build(17, 18, 19, 20, 21, 22),
            Vertex.build(23, 24, 25, 26, 27, 28)
    );

    private Entity entity;

    @Before
    public void setup() {
        entity = new VertexListEntity(VERTICES, DRAW_ORDER);
    }

    @Test
    public void elementDataShouldComeFromDrawOrder() {
        assertThat(entity.getElementData(), is(DRAW_ORDER));
    }

    @Test
    public void vertexCountShouldBeLengthOfVertexList() {
        assertThat(entity.getVertexCount(), is(2));
    }

    @Test
    public void vertexDataShouldComeFromVertexList() {
        // This test depends on the behavior of Vertex.data()
        final double[] expectedData = new double[]{
                17, 18, 19, 20, 21, 22,
                23, 24, 25, 26, 27, 28
        };

        assertThat(entity.getVertexData(), is(expectedData));
    }
}