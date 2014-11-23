package com.aimlessblade.cosmos.render;

import com.aimlessblade.cosmos.physics.Movable;

import java.util.Arrays;
import java.util.List;

public final class Bodies {
    private Bodies() {}

    public static RigidBody tetrahedron(final Movable pose) {
        final List<Vertex> vertexList = Arrays.asList(
                Vertex.build(0, 0, 1, 1, 0, 0),
                Vertex.build(0, 1, -1, 0, 1, 0),
                Vertex.build(1, -1, -1, 0, 0, 1),
                Vertex.build(-1, -1, -1, 1, 1, 1)
        );
        final int[] drawOrder = new int[] {
                0, 1, 2,
                0, 2, 3,
                0, 3, 1,
                1, 3, 2
        };
        return new RigidBody(pose, new VertexListEntity(vertexList, drawOrder));
    }

    public static RigidBody octahedron(final Movable pose) {
        final List<Vertex> vertexList = Arrays.asList(
                Vertex.build(0, 0, 1, 1, 0, 0),
                Vertex.build(0, 0, -1, 0, 1, 0),
                Vertex.build(0, 1, 0, 0, 0, 1),
                Vertex.build(0, -1, 0, 1, 1, 0),
                Vertex.build(1, 0, 0, 1, 0, 1),
                Vertex.build(-1, 0, 0, 0, 1, 1)
        );
        final int[] drawOrder = new int[] {
                0, 2, 4,
                0, 4, 3,
                0, 3, 5,
                0, 5, 2,
                1, 2, 5,
                1, 5, 3,
                1, 3, 4,
                1, 4, 2
        };
        return new RigidBody(pose, new VertexListEntity(vertexList, drawOrder));
    }
}
