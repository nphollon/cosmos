package com.aimlessblade.cosmos.graphics.vertex;

import com.aimlessblade.cosmos.graphics.Entity;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class VertexListEntityTest {
    private static final List<Integer> DRAW_ORDER = Arrays.asList(1, 0);
    private static final List<Double> VERTEX_DATA = Arrays.asList(17., 18., 23., 24.);

    private Entity entity;
    private List<Vertex> vertices;

    @Before
    public void setup() throws Exception {
        VertexType factory = new VertexType(new AttributeType("dummy", 2));
        vertices = new ArrayList<>();
        vertices.add(factory.build(Arrays.asList(17., 18.)));
        vertices.add(factory.build(Arrays.asList(23., 24.)));

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