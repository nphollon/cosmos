package com.aimlessblade.cosmos.app;

import com.aimlessblade.cosmos.graphics.vertex.VertexDataException;

final class Application {
    public static void main(final String[] args) throws VertexDataException {
        Window window = new Window(800, 600);
        window.loop(ProcessorFactory.build());
    }
}
