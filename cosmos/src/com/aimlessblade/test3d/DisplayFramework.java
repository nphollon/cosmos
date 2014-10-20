package com.aimlessblade.test3d;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
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

    public abstract void initialize() throws IOException;

    public abstract void draw();

    public abstract void event();


    public final void start() throws IOException, LWJGLException {
        initializeDisplay();
        initialize();

        while (!Display.isCloseRequested()) {
            clearDisplay();
            updateDisplay();
            pollInput();
        }

        Display.destroy();
    }

    private void pollInput() {
        while (Keyboard.next()) {
            event();
        }
    }

    private void updateDisplay() {
        draw();
        Display.sync(60);
        Display.update();
    }

    private void clearDisplay() {
        glClearColor(0, 0, 0, 0);
        glClearDepth(0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    private void initializeDisplay() throws LWJGLException {
        PixelFormat pixelFormat = new PixelFormat();
        ContextAttribs contextAtrributes = new ContextAttribs(3, 0)
                .withForwardCompatible(true);

        Display.setDisplayMode(new DisplayMode(width, height));
        Display.create(pixelFormat, contextAtrributes);
        glViewport(0, 0, width, height);
    }
}
