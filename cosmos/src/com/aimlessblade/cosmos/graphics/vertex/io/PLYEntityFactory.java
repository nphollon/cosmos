package com.aimlessblade.cosmos.graphics.vertex.io;

import com.aimlessblade.cosmos.graphics.Entity;

import java.io.BufferedReader;
import java.io.IOException;

public final class PLYEntityFactory {
    private final PLYParser parser;

    public PLYEntityFactory() {
        this(new PLYParser());
    }

    PLYEntityFactory(final PLYParser parser) {
        this.parser = parser;
    }

    public Entity buildEntity(final BufferedReader reader) throws IOException, PLYParseError {
        final PLYHeader header = parser.readHeader(reader);
        return parser.buildEntityFromBody(reader, header);
    }
}
