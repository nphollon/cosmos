package com.aimlessblade.cosmos.app;

import com.aimlessblade.cosmos.processor.Processor;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import static org.lwjgl.opengl.GL11.*;

final class Window {

    Window(final int width, final int height) {
        try {
            final PixelFormat pixelFormat = new PixelFormat();
            final ContextAttribs contextAtrributes = new ContextAttribs(3, 0)
                    .withForwardCompatible(true);
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.create(pixelFormat, contextAtrributes);
            glViewport(0, 0, width, height);
        } catch (LWJGLException e) {
            throw new ApplicationException("Could not create window", e);
        }
    }

    void loop(Processor processor) {
        while (!Display.isCloseRequested()) {
            clearDisplay();
            processor.run();
            updateDisplay();
        }
        Display.destroy();
    }

    void clearDisplay() {
        glClearColor(0.04f, 0.06f, 0.04f, 0);
        glClearDepth(1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    void updateDisplay() {
        Display.sync(60);
        Display.update();
    }
}
