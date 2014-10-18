package com.aimlessblade.test3d;

import org.lwjgl.LWJGLException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.aimlessblade.test3d.Color.*;

public class DepthExample extends DisplayFramework {

    public static final int SCREEN_WIDTH = 800;
    private static final double ASPECT_RATIO = 1.5;
    private static final double FRUSTUM_SCALE = 1;
    private static final double Z_NEAR = 1;
    private static final double Z_FAR = 3;

    private static final double SHORT = 0.2;
    private static final double LONG = 0.8;
    private static final double CENTER = 0.0;
    private static final double NEAR = -1.25;
    private static final double FAR = -1.75;

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

    private static final DrawableObject horizontalPrism = new DrawableObject(vertices1, elementList);
    private static final DrawableObject verticalPrism = new DrawableObject(vertices2, elementList);

    private WorldRenderer renderer;

    public static void main(String[] args) {
        try {
            new DepthExample(SCREEN_WIDTH, (int)(SCREEN_WIDTH / ASPECT_RATIO)).start();
        } catch (IOException|LWJGLException e) {
            e.printStackTrace();
        }
    }

    public DepthExample(int width, int height) {
        super(width, height);
    }

    @Override
    public void initialize() throws IOException {
        Program program = ProgramFactory.build("simple.vert", "simple.frag");
        Camera camera = new Camera(FRUSTUM_SCALE, Z_NEAR, Z_FAR, ASPECT_RATIO);
        World world = new World(horizontalPrism, verticalPrism);
        renderer = new WorldRenderer(world, program, camera);

        horizontalPrism.setOffset(0, 0, -1);
        verticalPrism.setOffset(0, 0, -0.8);
    }

    @Override
    public void draw() {
        renderer.draw();
    }
}
