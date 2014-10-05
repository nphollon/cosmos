package com.aimlessblade.test3d;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public abstract class DisplayFramework {
    private final int width;
    private final int height;

    public DisplayFramework(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void start() throws IOException, LWJGLException {
        initializeDisplay();
        initialize();

        while (!Display.isCloseRequested()) {
            glClear(GL_COLOR_BUFFER_BIT);
            draw();
            Display.sync(60);
            Display.update();
        }

        Display.destroy();
    }

    public abstract void initialize() throws IOException;

    public abstract void draw();


    private void initializeDisplay() throws LWJGLException {
        PixelFormat pixelFormat = new PixelFormat();
        ContextAttribs contextAtrributes = new ContextAttribs(3, 0)
                .withForwardCompatible(true);

        Display.setDisplayMode(new DisplayMode(width, height));
        Display.create(pixelFormat, contextAtrributes);
        glViewport(0, 0, width, height);
    }
}
