package com.aimlessblade.cosmos.app;

import com.aimlessblade.cosmos.geo.Camera;
import com.aimlessblade.cosmos.geo.PerspectiveCamera;
import com.aimlessblade.cosmos.geo.RigidBody;
import com.aimlessblade.cosmos.input.InputState;
import com.aimlessblade.cosmos.input.KeyboardEvent;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.aimlessblade.cosmos.input.InputState.angularImpulse;
import static com.aimlessblade.cosmos.input.InputState.impulse;
import static com.aimlessblade.cosmos.input.KeyboardEvent.lift;
import static com.aimlessblade.cosmos.input.KeyboardEvent.press;
import static org.lwjgl.opengl.GL11.*;

public final class Application {
    public static void main(final String[] args) {
        final Map<KeyboardEvent, Consumer<InputState>> keymap = new HashMap<>();
        keymap.put(press(Keyboard.KEY_A), impulse(1, 0, 0));
        keymap.put(lift(Keyboard.KEY_A), impulse(-1, 0, 0));
        keymap.put(press(Keyboard.KEY_D), impulse(-1, 0, 0));
        keymap.put(lift(Keyboard.KEY_D), impulse(1, 0, 0));
        
        keymap.put(press(Keyboard.KEY_W), impulse(0, 1, 0));
        keymap.put(lift(Keyboard.KEY_W), impulse(0, -1, 0));
        keymap.put(press(Keyboard.KEY_S), impulse(0, -1, 0));
        keymap.put(lift(Keyboard.KEY_S), impulse(0, 1, 0));
        
        keymap.put(press(Keyboard.KEY_Q), impulse(0, 0, 1));
        keymap.put(lift(Keyboard.KEY_Q), impulse(0, 0, -1));
        keymap.put(press(Keyboard.KEY_E), impulse(0, 0, -1));
        keymap.put(lift(Keyboard.KEY_E), impulse(0, 0, 1));
        
        keymap.put(press(Keyboard.KEY_I), angularImpulse(1, 0, 0));
        keymap.put(lift(Keyboard.KEY_I), angularImpulse(-1, 0, 0));
        keymap.put(press(Keyboard.KEY_K), angularImpulse(-1, 0, 0));
        keymap.put(lift(Keyboard.KEY_K), angularImpulse(1, 0, 0));
        
        keymap.put(press(Keyboard.KEY_J), angularImpulse(0, 1, 0));
        keymap.put(lift(Keyboard.KEY_J), angularImpulse(0, -1, 0));
        keymap.put(press(Keyboard.KEY_L), angularImpulse(0, -1, 0));
        keymap.put(lift(Keyboard.KEY_L), angularImpulse(0, 1, 0));
        
        keymap.put(press(Keyboard.KEY_U), angularImpulse(0, 0, 1));
        keymap.put(lift(Keyboard.KEY_U), angularImpulse(0, 0, -1));
        keymap.put(press(Keyboard.KEY_O), angularImpulse(0, 0, -1));
        keymap.put(lift(Keyboard.KEY_O), angularImpulse(0, 0, 1));

        final File vertexShader = new File("/home/nick/IdeaProjects/cosmos/cosmos/shaders/simple.vert");
        final File fragmentShader = new File("/home/nick/IdeaProjects/cosmos/cosmos/shaders/simple.frag");
        final ProcessorFactory factory = new ProcessorFactory(keymap, vertexShader, fragmentShader);

        final Camera camera = new PerspectiveCamera(1.0, 0.1, 50.0, 1.5);

        final List<RigidBody> entities = new ArrayList<>();

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
