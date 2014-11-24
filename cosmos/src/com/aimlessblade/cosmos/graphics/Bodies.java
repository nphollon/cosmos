package com.aimlessblade.cosmos.graphics;

import com.aimlessblade.cosmos.graphics.vertex.AttributeInfo;
import com.aimlessblade.cosmos.graphics.vertex.Vertex;
import com.aimlessblade.cosmos.graphics.vertex.VertexFactory;
import com.aimlessblade.cosmos.graphics.vertex.VertexListEntity;
import com.aimlessblade.cosmos.physics.Movable;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public final class Bodies {
    private final VertexFactory vertexFactory;

    public Bodies() {
        vertexFactory = new VertexFactory(new AttributeInfo("position", 3), new AttributeInfo("color", 3));
    }

    public RigidBody tetrahedron(final Movable pose) {

        final List<Vertex> vertexList = Arrays.asList(
                vertexFactory.build(Arrays.asList(0., 0., 1., 1., 0., 0.)),
                vertexFactory.build(Arrays.asList(0., 1., -1., 0., 1., 0.)),
                vertexFactory.build(Arrays.asList(1., -1., -1., 0., 0., 1.)),
                vertexFactory.build(Arrays.asList(-1., -1., -1., 1., 1., 1.))
        );
        final List<Integer> drawOrder = Arrays.asList(
                0, 1, 2,
                0, 2, 3,
                0, 3, 1,
                1, 3, 2
        );
        return new RigidBody(pose, new VertexListEntity(vertexList, drawOrder));
    }

    public RigidBody octahedron(final Movable pose) {
        final List<Vertex> vertexList = Arrays.asList(
                vertexFactory.build(Arrays.asList(0., 0., 1., 1., 0., 0.)),
                vertexFactory.build(Arrays.asList(0., 0., -1., 0., 1., 0.)),
                vertexFactory.build(Arrays.asList(0., 1., 0., 0., 0., 1.)),
                vertexFactory.build(Arrays.asList(0., -1., 0., 1., 1., 0.)),
                vertexFactory.build(Arrays.asList(1., 0., 0., 1., 0., 1.)),
                vertexFactory.build(Arrays.asList(-1., 0., 0., 0., 1., 1.))
        );
        final List<Integer> drawOrder = Arrays.asList(
                0, 2, 4,
                0, 4, 3,
                0, 3, 5,
                0, 5, 2,
                1, 2, 5,
                1, 5, 3,
                1, 3, 4,
                1, 4, 2
        );
        return new RigidBody(pose, new VertexListEntity(vertexList, drawOrder));
    }
}
