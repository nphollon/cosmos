package com.aimlessblade.cosmos.app;

import com.aimlessblade.cosmos.camera.Camera;
import com.aimlessblade.cosmos.camera.PerspectiveCamera;
import com.aimlessblade.cosmos.input.InputState;
import com.aimlessblade.cosmos.input.KeyboardEvent;
import com.aimlessblade.cosmos.physics.Movable;
import com.aimlessblade.cosmos.physics.Vectors;
import com.aimlessblade.cosmos.physics.Velocity;
import com.aimlessblade.cosmos.render.RigidBody;
import com.aimlessblade.cosmos.render.Vertex;
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

import static com.aimlessblade.cosmos.input.InputState.angularImpulse;
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
        final Movable pose = Vectors.pose(0, 0, -6, 0, 0, 0);
        final List<Vertex> vertexList = Arrays.asList(
                Vertex.build(0, 0, 1, 1, 0, 0),
                Vertex.build(0, 1, -1, 0, 1, 0),
                Vertex.build(1, -1, -1, 0, 0, 1),
                Vertex.build(-1, -1, -1, 1, 1, 1)
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
        final double v = 1;
        
        final Velocity west = Vectors.velocity(-v, 0, 0);
        final Velocity east = Vectors.velocity(v, 0, 0);
        final Velocity up = Vectors.velocity(0, v, 0);
        final Velocity down = Vectors.velocity(0, -v, 0);
        final Velocity north = Vectors.velocity(0, 0, -v);
        final Velocity south = Vectors.velocity(0, 0, v);

        final Map<KeyboardEvent, Consumer<InputState>> keymap = new HashMap<>();
        keymap.put(press(Keyboard.KEY_A), angularImpulse(west));
        keymap.put(lift(Keyboard.KEY_A), angularImpulse(east));
        keymap.put(press(Keyboard.KEY_D), angularImpulse(east));
        keymap.put(lift(Keyboard.KEY_D), angularImpulse(west));

        keymap.put(press(Keyboard.KEY_W), angularImpulse(up));
        keymap.put(lift(Keyboard.KEY_W), angularImpulse(down));
        keymap.put(press(Keyboard.KEY_S), angularImpulse(down));
        keymap.put(lift(Keyboard.KEY_S), angularImpulse(up));

        keymap.put(press(Keyboard.KEY_Q), angularImpulse(north));
        keymap.put(lift(Keyboard.KEY_Q), angularImpulse(south));
        keymap.put(press(Keyboard.KEY_E), angularImpulse(south));
        keymap.put(lift(Keyboard.KEY_E), angularImpulse(north));

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
