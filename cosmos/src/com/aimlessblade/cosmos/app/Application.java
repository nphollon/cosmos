package com.aimlessblade.cosmos.app;

final class Application {
    public static void main(final String[] args) {
        Window window = new Window(800, 600);
        window.loop(ProcessorFactory.build());
    }
}
