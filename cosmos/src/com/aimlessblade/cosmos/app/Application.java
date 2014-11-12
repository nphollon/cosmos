package com.aimlessblade.cosmos.app;

import com.aimlessblade.cosmos.geo.*;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.aimlessblade.cosmos.input.InputState.impulse;
import static com.aimlessblade.cosmos.input.KeyboardEvent.lift;
import static com.aimlessblade.cosmos.input.KeyboardEvent.press;
import static org.lwjgl.opengl.GL11.*;

public final class Application {
    public static void main(final String[] args) {
        final ProcessorFactory factory = buildProcessorFactory();

        final Camera camera = new PerspectiveCamera(1.0, 1, 50.0, 1.5);

        final List<RigidBody> entities = buildEntityList();

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

    private static List<RigidBody> buildEntityList() {
        final Pose pose = Pose.build(Displacement.cartesian(0, 0, -1), Orientation.zero());
        final List<Vertex> vertexList = Arrays.asList(
                Vertex.build(0, 0, -5, 1, 0, 0),
                Vertex.build(0, 1, -6, 0, 1, 0),
                Vertex.build(1, -1, -6, 0, 0, 1),
                Vertex.build(-1, -1, -6, 1, 1, 1)
        );
        final int[] drawOrder = new int[] {
                0, 1, 2,
                0, 2, 3,
                0, 3, 1,
                1, 3, 2
        };
        final RigidBody body = new RigidBody(pose, vertexList, drawOrder);

        return Arrays.asList(body);
    }

    private static ProcessorFactory buildProcessorFactory() {
        final Velocity west = Velocity.cartesian(-3, 0, 0);
        final Velocity east = Velocity.cartesian(3, 0, 0);
        final Velocity up = Velocity.cartesian(0, 3, 0);
        final Velocity down = Velocity.cartesian(0, -3, 0);
        final Velocity north = Velocity.cartesian(0, 0, -3);
        final Velocity south = Velocity.cartesian(0, 0, 3);

        final Map<KeyboardEvent, Consumer<InputState>> keymap = new HashMap<>();
        keymap.put(press(Keyboard.KEY_A), impulse(west));
        keymap.put(lift(Keyboard.KEY_A), impulse(east));
        keymap.put(press(Keyboard.KEY_D), impulse(east));
        keymap.put(lift(Keyboard.KEY_D), impulse(west));

        keymap.put(press(Keyboard.KEY_W), impulse(up));
        keymap.put(lift(Keyboard.KEY_W), impulse(down));
        keymap.put(press(Keyboard.KEY_S), impulse(down));
        keymap.put(lift(Keyboard.KEY_S), impulse(up));

        keymap.put(press(Keyboard.KEY_Q), impulse(north));
        keymap.put(lift(Keyboard.KEY_Q), impulse(south));
        keymap.put(press(Keyboard.KEY_E), impulse(south));
        keymap.put(lift(Keyboard.KEY_E), impulse(north));

        final File vertexShader = new File("/home/nick/IdeaProjects/cosmos/cosmos/shaders/simple.vert");
        final File fragmentShader = new File("/home/nick/IdeaProjects/cosmos/cosmos/shaders/simple.frag");
        return new ProcessorFactory(keymap, vertexShader, fragmentShader);
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
