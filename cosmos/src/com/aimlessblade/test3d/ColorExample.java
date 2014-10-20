package com.aimlessblade.test3d;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.aimlessblade.test3d.Color.*;

public class ColorExample extends DisplayFramework {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final float ASPECT_RATIO = (float) WIDTH / HEIGHT;

    private static final double FRUSTUM_SCALE = 1.0;
    private static final double Z_NEAR = 0.1;
    private static final double Z_FAR = 50.0;

    private static final double LEFT = -0.25;
    private static final double RIGHT = 0.25;
    private static final double UP = 0.25;
    private static final double DOWN = -0.25;
    private static final double FRONT = 0.75;
    private static final double BACK = -0.75;

    private static final Position UFL = new Position(LEFT, UP, FRONT);
    private static final Position UFR = new Position(RIGHT, UP, FRONT);
    private static final Position UBL = new Position(LEFT, UP, BACK);
    private static final Position UBR = new Position(RIGHT, UP, BACK);
    private static final Position DFL = new Position(LEFT, DOWN, FRONT);
    private static final Position DFR = new Position(RIGHT, DOWN, FRONT);
    private static final Position DBL = new Position(LEFT, DOWN, BACK);
    private static final Position DBR = new Position(RIGHT, DOWN, BACK);

    private static final List<Vertex> vertices = Arrays.asList(
            new Vertex(UFL, BLUE),
            new Vertex(UBL, BLUE),
            new Vertex(UBR, BLUE),
            new Vertex(UFR, BLUE),

            new Vertex(DFL, GRAY),
            new Vertex(DFR, GRAY),
            new Vertex(DBR, GRAY),
            new Vertex(DBL, GRAY),

            new Vertex(UFL, GREEN),
            new Vertex(DFL, GREEN),
            new Vertex(DBL, GREEN),
            new Vertex(UBL, GREEN),

            new Vertex(UFR, YELLOW),
            new Vertex(UBR, YELLOW),
            new Vertex(DBR, YELLOW),
            new Vertex(DFR, YELLOW),

            new Vertex(UFL, RED),
            new Vertex(UFR, RED),
            new Vertex(DFR, RED),
            new Vertex(DFL, RED),

            new Vertex(UBL, CYAN),
            new Vertex(DBL, CYAN),
            new Vertex(DBR, CYAN),
            new Vertex(UBR, CYAN)
    );

    private static final List<Integer> drawOrder = Arrays.asList(
            0, 1, 2,
            2, 3, 0,

            4, 5, 6,
            6, 7, 4,

            8, 9, 10,
            10, 11, 8,

            12, 13, 14,
            14, 15, 12,

            16, 17, 18,
            18, 19, 16,

            20, 21, 22,
            22, 23, 20
    );

    private static final DrawableObject OBJECT = new DrawableObject(vertices, drawOrder);
    private static final double SPIN_INCREMENT = 2;
    private static final double NUDGE_INCREMENT = 0.2;

    private int roll;
    private int yaw;
    private int pitch;
    private int nudgeX;
    private int nudgeY;
    private int nudgeZ;
    private WorldRenderer renderer;

    public ColorExample(int width, int height) {
        super(width, height);
    }

    public static void main(String[] argv) {
        ColorExample colorExample = new ColorExample(WIDTH, HEIGHT);
        try {
            colorExample.start();
        } catch (IOException | LWJGLException e) {
            e.printStackTrace();
        }
    }

    public void initialize() throws IOException {
        Program program = ProgramFactory.build("simple.vert", "simple.frag");
        Camera camera = new Camera(FRUSTUM_SCALE, Z_NEAR, Z_FAR, ASPECT_RATIO);

        OBJECT.nudge(1, 1, -5);
        World world = new World(OBJECT);
        renderer = new WorldRenderer(world, program, camera);
    }

    public void draw () {
        OBJECT.nudge(nudgeX * NUDGE_INCREMENT, nudgeY * NUDGE_INCREMENT, nudgeZ * NUDGE_INCREMENT);
        OBJECT.spin(pitch * SPIN_INCREMENT, yaw * SPIN_INCREMENT, roll * SPIN_INCREMENT);
        renderer.draw();
    }

    public void event() {
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
            default: break;
        }
    }
}
