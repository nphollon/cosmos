package com.aimlessblade.cosmos.app;

import com.aimlessblade.cosmos.camera.Camera;
import com.aimlessblade.cosmos.camera.PerspectiveCamera;
import com.aimlessblade.cosmos.input.InputState;
import com.aimlessblade.cosmos.input.KeyboardEvent;
import com.aimlessblade.cosmos.physics.Vectors;
import com.aimlessblade.cosmos.physics.Velocity;
import com.aimlessblade.cosmos.render.Bodies;
import com.aimlessblade.cosmos.render.RigidBody;
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

        final RigidBody tetrahedron = Bodies.tetrahedron(Vectors.pose(0, 0, -6, 0, 0, 0));
        final RigidBody octahedron = Bodies.octahedron(Vectors.pose(1, 2, -3, 0, 0, 0));

        final List<RigidBody> entities = Arrays.asList(octahedron, tetrahedron);

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

    private static ProcessorFactory buildProcessorFactory() {
        final double v = 1;
        
        final Velocity west = Vectors.velocity(-v, 0, 0);
        final Velocity east = Vectors.velocity(v, 0, 0);
        final Velocity up = Vectors.velocity(0, v, 0);
        final Velocity down = Vectors.velocity(0, -v, 0);
        final Velocity north = Vectors.velocity(0, 0, -v);
        final Velocity south = Vectors.velocity(0, 0, v);

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
        glClearColor(0.04f, 0.06f, 0.04f, 0);
        glClearDepth(1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    private static void updateDisplay() {
        Display.sync(60);
        Display.update();
    }
}
