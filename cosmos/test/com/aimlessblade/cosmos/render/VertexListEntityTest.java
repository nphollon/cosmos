package com.aimlessblade.cosmos.render;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class VertexListEntityTest {
    private static final int[] DRAW_ORDER = new int[]{12, 13, 14, 15, 16};
    private static final double[] VERTEX_DATA = new double[]{
            17, 18, 19, 20, 21, 22,
            23, 24, 25, 26, 27, 28
    };

    private Entity entity;
    private List<Vertex> vertices;

    @Before
    public void setup() {
        vertices = new ArrayList<>();
        vertices.add(Vertex.build(17, 18, 19, 20, 21, 22));
        vertices.add(Vertex.build(23, 24, 25, 26, 27, 28));

        entity = new VertexListEntity(vertices, DRAW_ORDER);
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
        assertThat(entity.getVertexData(), is(VERTEX_DATA));
    }

    @Test
    public void vertexDataShouldBeImmutable() {
        // Of course, the array is still mutable :-/
        vertices.remove(1);
        assertThat(entity.getVertexData(), is(VERTEX_DATA));
    }
}