package com.aimlessblade.cosmos.graphics;

import com.aimlessblade.cosmos.graphics.vertex.*;
import com.aimlessblade.cosmos.graphics.vertex.io.PLYEntityFactory;
import com.aimlessblade.cosmos.graphics.vertex.io.PLYParseError;
import com.aimlessblade.cosmos.physics.Movable;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Getter
public final class Bodies {
    private final VertexType vertexType;

    public Bodies() {
        vertexType = new VertexType(new AttributeType("position", 3), new AttributeType("color", 3));
    }

    public RigidBody tetrahedron(final Movable pose) throws VertexDataException {
        final Entity entity;
        final PLYEntityFactory plyEntityFactory = new PLYEntityFactory();

        try {
            entity = plyEntityFactory.buildEntity(new BufferedReader(new FileReader("cosmos/models/simpletetrahedron.ply")));
            return new RigidBody(pose, entity);
        } catch (IOException|PLYParseError e) {
            throw new RuntimeException(e);
        }
    }

    public RigidBody octahedron(final Movable pose) throws VertexDataException {
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
