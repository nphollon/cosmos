package com.aimlessblade.cosmos.app;

import com.aimlessblade.cosmos.geo.Camera;
import com.aimlessblade.cosmos.geo.Entity;
import lombok.AllArgsConstructor;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import java.io.IOException;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

@AllArgsConstructor
public final class DisplayFramework {
    private final int width;
    private final int height;
    private final ProcessorFactory processorFactory;

    public void start(final Camera camera, final List<Entity> entities) throws IOException, LWJGLException {
        initializeDisplay();

        final Processor processor = processorFactory.build(camera, entities);

        while (!Display.isCloseRequested()) {
            clearDisplay();
            processor.run();
            updateDisplay();
        }

        Display.destroy();
    }

    private void initializeDisplay() throws LWJGLException {
        final PixelFormat pixelFormat = new PixelFormat();
        final ContextAttribs contextAtrributes = new ContextAttribs(3, 0)
                .withForwardCompatible(true);

        Display.setDisplayMode(new DisplayMode(width, height));
        Display.create(pixelFormat, contextAtrributes);
        glViewport(0, 0, width, height);
    }

    private void clearDisplay() {
        glClearColor(0, 0, 0, 0);
        glClearDepth(0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    private void updateDisplay() {
        Display.sync(60);
        Display.update();
    }
}
