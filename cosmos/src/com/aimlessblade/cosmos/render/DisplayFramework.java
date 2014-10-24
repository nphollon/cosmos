package com.aimlessblade.cosmos.render;

import com.aimlessblade.cosmos.geo.Puppeteer;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class DisplayFramework {
    private final int width;
    private final int height;
    private final WorldRenderer renderer;
    private final Puppeteer puppeteer;

    public DisplayFramework(int width, int height, WorldRenderer renderer, Puppeteer puppeteer) {
        this.width = width;
        this.height = height;
        this.renderer = renderer;
        this.puppeteer = puppeteer;
    }

    public final void start() throws IOException, LWJGLException {
        initializeDisplay();
        renderer.initialize();

        while (!Display.isCloseRequested()) {
            clearDisplay();
            updateDisplay();
            pollInput();
        }

        Display.destroy();
    }

    private void pollInput() {
        while (Keyboard.next()) {
            puppeteer.event(Keyboard.getEventKey(), Keyboard.getEventKeyState());
        }

        puppeteer.move();
    }

    private void updateDisplay() {
        renderer.draw();
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
