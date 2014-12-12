package com.aimlessblade.cosmos.app;

import com.aimlessblade.cosmos.graphics.vertex.io.PLYParseError;

import java.io.IOException;

final class Application {
    public static void main(final String[] args) throws IOException, PLYParseError {
        Window window = new Window(800, 600);
        window.loop(ProcessorFactory.build());
    }
}
