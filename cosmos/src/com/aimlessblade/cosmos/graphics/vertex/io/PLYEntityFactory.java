package com.aimlessblade.cosmos.graphics.vertex.io;

import com.aimlessblade.cosmos.graphics.Entity;
import com.aimlessblade.cosmos.graphics.RigidBody;
import com.aimlessblade.cosmos.physics.Movable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public final class PLYEntityFactory {
    private final PLYParser parser;

    public PLYEntityFactory() {
        this(new PLYParser());
    }

    PLYEntityFactory(final PLYParser parser) {
        this.parser = parser;
    }

    public RigidBody buildRigidBody(final Movable pose, final String fileName) throws IOException, PLYParseError {
        final Entity entity = buildEntity(new BufferedReader(new FileReader(fileName)));
        return new RigidBody(pose, entity);
    }

    public Entity buildEntity(final BufferedReader reader) throws IOException, PLYParseError {
        final PLYHeader header = parser.readHeader(reader);
        return parser.buildEntityFromBody(reader, header);
    }
}
