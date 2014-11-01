package com.aimlessblade.cosmos.app;

import com.aimlessblade.cosmos.geo.*;
import com.aimlessblade.cosmos.input.Keymap;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public final class Application {
    public static void main(final String[] args) {
        final Keymap keymap = null;
        final File vertexShader = new File("/home/nick/IdeaProjects/cosmos/cosmos/shaders/simple.vert");
        final File fragmentShader = new File("/home/nick/IdeaProjects/cosmos/cosmos/shaders/simple.frag");
        final ProcessorFactory factory = new ProcessorFactory(keymap, vertexShader, fragmentShader);

        final Displacement cameraLocation = Displacement.cartesian(0, 0, 0);
        final Orientation cameraOrientation = Orientation.axisAngle(1, 0, 0, 0);
        final Camera camera = new PerspectiveCamera(cameraLocation, cameraOrientation);

        final List<Entity> entities = new ArrayList<>();

        try {
            createWindow(800, 600);
            final Processor processor = factory.build(camera, entities);
            loop(processor);
        } catch (final IOException | LWJGLException e) {
            e.printStackTrace();
        } finally {
            destroyWindow();
        }
    }

    private static void destroyWindow() {
        Display.destroy();
    }

    public static void loop(final Processor processor) throws IOException, LWJGLException {
        while (!Display.isCloseRequested()) {
            clearDisplay();
            processor.run();
            updateDisplay();
        }
    }

    public static void createWindow(final int width, final int height) throws LWJGLException {
        final PixelFormat pixelFormat = new PixelFormat();
        final ContextAttribs contextAtrributes = new ContextAttribs(3, 0)
                .withForwardCompatible(true);

        Display.setDisplayMode(new DisplayMode(width, height));
        Display.create(pixelFormat, contextAtrributes);
        glViewport(0, 0, width, height);
    }

    private static void clearDisplay() {
        glClearColor(0, 0, 0, 0);
        glClearDepth(0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    private static void updateDisplay() {
        Display.sync(60);
        Display.update();
    }
}
