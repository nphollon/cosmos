package com.aimlessblade.test3d;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.aimlessblade.test3d.Color.*;
import static org.lwjgl.opengl.GL11.*;

public class Sandbox {

    public static final int SCREEN_WIDTH = 800;
    private static final double ASPECT_RATIO = 1.5;
    private static final double FRUSTUM_SCALE = 1;
    private static final double Z_NEAR = 1;
    private static final double Z_FAR = 50;

    private static final double SHORT = 0.2;
    private static final double LONG = 0.8;
    private static final double CENTER = 0.0;
    private static final double NEAR = 0.25;
    private static final double FAR = -0.25;

    private static final Position TOP_POINT = new Position(CENTER, LONG, NEAR);
    private static final Position BOTTOM_POINT = new Position(CENTER, -LONG, NEAR);
    private static final Position TOP_LEFT_BASE = new Position(-SHORT, LONG, FAR);
    private static final Position TOP_RIGHT_BASE = new Position(SHORT, LONG, FAR);
    private static final Position BOTTOM_LEFT_BASE = new Position(-SHORT, -LONG, FAR);
    private static final Position BOTTOM_RIGHT_BASE = new Position(SHORT, -LONG, FAR);

    private static final Position LEFT_POINT = new Position(-LONG, CENTER, NEAR);
    private static final Position RIGHT_POINT = new Position(LONG, CENTER, NEAR);
    private static final Position LEFT_TOP_BASE = new Position(-LONG, SHORT, FAR);
    private static final Position LEFT_BOTTOM_BASE = new Position(-LONG, -SHORT, FAR);
    private static final Position RIGHT_TOP_BASE = new Position(LONG, SHORT, FAR);
    private static final Position RIGHT_BOTTOM_BASE = new Position(LONG, -SHORT, FAR);

    private static final List<Vertex> vertices1 = Arrays.asList(
            new Vertex(LEFT_POINT, GREEN),
            new Vertex(LEFT_BOTTOM_BASE, GREEN),
            new Vertex(LEFT_TOP_BASE, GREEN),

            new Vertex(RIGHT_POINT, GRAY),
            new Vertex(RIGHT_TOP_BASE, GRAY),
            new Vertex(RIGHT_BOTTOM_BASE, GRAY),

            new Vertex(LEFT_POINT, RED),
            new Vertex(LEFT_TOP_BASE, RED),
            new Vertex(RIGHT_TOP_BASE, RED),
            new Vertex(RIGHT_POINT, RED),

            new Vertex(LEFT_POINT, YELLOW),
            new Vertex(RIGHT_POINT, YELLOW),
            new Vertex(RIGHT_BOTTOM_BASE, YELLOW),
            new Vertex(LEFT_BOTTOM_BASE, YELLOW),

            new Vertex(LEFT_TOP_BASE, CYAN),
            new Vertex(LEFT_BOTTOM_BASE, CYAN),
            new Vertex(RIGHT_BOTTOM_BASE, CYAN),
            new Vertex(RIGHT_TOP_BASE, CYAN)
    );

    private static final List<Vertex> vertices2 = Arrays.asList(
            new Vertex(TOP_POINT, GREEN),
            new Vertex(TOP_LEFT_BASE, GREEN),
            new Vertex(TOP_RIGHT_BASE, GREEN),

            new Vertex(BOTTOM_POINT, RED),
            new Vertex(BOTTOM_RIGHT_BASE, RED),
            new Vertex(BOTTOM_LEFT_BASE, RED),

            new Vertex(TOP_POINT, BLUE),
            new Vertex(TOP_RIGHT_BASE, BLUE),
            new Vertex(BOTTOM_RIGHT_BASE, BLUE),
            new Vertex(BOTTOM_POINT, BLUE),

            new Vertex(TOP_POINT, GRAY),
            new Vertex(BOTTOM_POINT, GRAY),
            new Vertex(BOTTOM_LEFT_BASE, GRAY),
            new Vertex(TOP_LEFT_BASE, GRAY),

            new Vertex(TOP_RIGHT_BASE, CYAN),
            new Vertex(TOP_LEFT_BASE, CYAN),
            new Vertex(BOTTOM_LEFT_BASE, CYAN),
            new Vertex(BOTTOM_RIGHT_BASE, CYAN)
    );

    private static final List<Integer> elementList = Arrays.asList(
            0, 1, 2,

            3, 4, 5,

            6, 7, 8,
            8, 9, 6,

            10, 11, 12,
            12, 13, 10,

            14, 15, 16,
            16, 17, 14
    );

    private static final DrawableObject[] ENTITIES = new DrawableObject[] {
            new DrawableObject(vertices1, elementList),
            new DrawableObject(vertices2, elementList)
    };

    private static final double SPIN_INCREMENT = 2;
    private static final double NUDGE_INCREMENT = 0.2;
    private final int width;
    private final int height;

    private int roll;
    private int yaw;
    private int pitch;
    private int nudgeX;
    private int nudgeY;
    private int nudgeZ;
    private WorldRenderer renderer;
    private DrawableObject selectedEntity;

    public Sandbox(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public static void main(String[] argv) {
        Sandbox sandbox = new Sandbox(SCREEN_WIDTH, (int)(SCREEN_WIDTH / ASPECT_RATIO));
        try {
            sandbox.start();
        } catch (IOException | LWJGLException e) {
            e.printStackTrace();
        }
    }

    public final void start() throws IOException, LWJGLException {
        initializeDisplay();
        initializeWorld();

        while (!Display.isCloseRequested()) {
            updateDisplay();

            while (Keyboard.next()) {
                event();
            }
        }

        Display.destroy();
    }

    private void initializeDisplay() throws LWJGLException {
        PixelFormat pixelFormat = new PixelFormat();
        ContextAttribs contextAtrributes = new ContextAttribs(3, 0)
                .withForwardCompatible(true);

        Display.setDisplayMode(new DisplayMode(width, height));
        Display.create(pixelFormat, contextAtrributes);
        glViewport(0, 0, width, height);
    }

    private void initializeWorld() throws IOException {
        Program program = ProgramFactory.build("simple.vert", "simple.frag");
        Camera camera = new Camera(FRUSTUM_SCALE, Z_NEAR, Z_FAR, ASPECT_RATIO);

        for (DrawableObject entity : ENTITIES) {
            entity.nudge(0, 0, -3);
        }

        selectedEntity = ENTITIES[0];
        renderer = new WorldRenderer(new World(ENTITIES), program, camera);
    }

    private void updateDisplay() {
        glClearColor(0, 0, 0, 0);
        glClearDepth(0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        selectedEntity.nudge(nudgeX * NUDGE_INCREMENT, nudgeY * NUDGE_INCREMENT, nudgeZ * NUDGE_INCREMENT);
        selectedEntity.spin(pitch * SPIN_INCREMENT, yaw * SPIN_INCREMENT, roll * SPIN_INCREMENT);
        renderer.draw();

        Display.sync(60);
        Display.update();
    }

    private void event() {
        int shiftDirection = Keyboard.getEventKeyState() ? 1 : -1;

        switch (Keyboard.getEventKey()) {
            case Keyboard.KEY_A:
                yaw += shiftDirection;
                break;
            case Keyboard.KEY_D:
                yaw -= shiftDirection;
                break;
            case Keyboard.KEY_W:
                pitch += shiftDirection;
                break;
            case Keyboard.KEY_S:
                pitch -= shiftDirection;
                break;
            case Keyboard.KEY_Q:
                roll += shiftDirection;
                break;
            case Keyboard.KEY_E:
                roll -= shiftDirection;
                break;
            case Keyboard.KEY_J:
                nudgeX -= shiftDirection;
                break;
            case Keyboard.KEY_L:
                nudgeX += shiftDirection;
                break;
            case Keyboard.KEY_I:
                nudgeY += shiftDirection;
                break;
            case Keyboard.KEY_K:
                nudgeY -= shiftDirection;
                break;
            case Keyboard.KEY_U:
                nudgeZ += shiftDirection;
                break;
            case Keyboard.KEY_O:
                nudgeZ -= shiftDirection;
                break;
            case Keyboard.KEY_1:
                selectedEntity = ENTITIES[0];
                break;
            case Keyboard.KEY_2:
                selectedEntity = ENTITIES[1];
                break;
            default: break;
        }
    }
}
