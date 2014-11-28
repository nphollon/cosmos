package com.aimlessblade.cosmos.graphics.vertex.io;

import com.aimlessblade.cosmos.graphics.Entity;

import java.io.Reader;

public final class PLYEntityFactory {
    private final PLYParser parser;

    public PLYEntityFactory() {
        this(new PLYParser());
    }

    PLYEntityFactory(final PLYParser parser) {
        this.parser = parser;
    }

    public Entity buildEntity(final Reader reader) {
        final PLYHeader header = parser.readHeader(reader);
        return parser.buildEntityFromBody(reader, header);
    }
}
