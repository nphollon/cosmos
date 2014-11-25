package com.aimlessblade.cosmos.graphics;

import com.aimlessblade.cosmos.graphics.vertex.AttributeType;
import com.aimlessblade.cosmos.graphics.vertex.Vertex;
import com.aimlessblade.cosmos.graphics.vertex.VertexListEntity;
import com.aimlessblade.cosmos.graphics.vertex.VertexType;
import com.aimlessblade.cosmos.physics.Movable;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public final class Bodies {
    private final VertexType vertexType;

    public Bodies() {
        vertexType = new VertexType(new AttributeType("position", 3), new AttributeType("color", 3));
    }

    public RigidBody tetrahedron(final Movable pose) {

        final List<Vertex> vertexList = Arrays.asList(
                vertexType.build(Arrays.asList(0., 0., 1., 1., 0., 0.)),
                vertexType.build(Arrays.asList(0., 1., -1., 0., 1., 0.)),
                vertexType.build(Arrays.asList(1., -1., -1., 0., 0., 1.)),
                vertexType.build(Arrays.asList(-1., -1., -1., 1., 1., 1.))
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
                vertexType.build(Arrays.asList(0., 0., 1., 1., 0., 0.)),
                vertexType.build(Arrays.asList(0., 0., -1., 0., 1., 0.)),
                vertexType.build(Arrays.asList(0., 1., 0., 0., 0., 1.)),
                vertexType.build(Arrays.asList(0., -1., 0., 1., 1., 0.)),
                vertexType.build(Arrays.asList(1., 0., 0., 1., 0., 1.)),
                vertexType.build(Arrays.asList(-1., 0., 0., 0., 1., 1.))
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
