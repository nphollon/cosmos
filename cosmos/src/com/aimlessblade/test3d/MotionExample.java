package com.aimlessblade.test3d;

import org.lwjgl.LWJGLException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MotionExample extends DisplayFramework {

    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final float ASPECT_RATIO = 1.0f;
    private static final float FRUSTUM_SCALE = 1;
    private static final float Z_NEAR = 1;
    private static final float Z_FAR = 3;

    private static final long START_TIME = System.currentTimeMillis();
    private static final float PERIOD = 5.0f;

    private static final List<Vertex> TRIANGLE_VERTICES = Arrays.asList(
            new Vertex(new Position(0, 0.5f, 0), Color.BLUE),
            new Vertex(new Position(0.5f, -0.366f, 0), Color.RED),
            new Vertex(new Position(-0.5f, -0.366f, 0), Color.GREEN)
    );

    private static final List<Integer> TRIANGLE_ELEMENTS = Arrays.asList(0, 1, 2);

    private static final DrawableObject TRIANGLE = new DrawableObject(TRIANGLE_VERTICES, TRIANGLE_ELEMENTS);
    private static final World WORLD = new World(TRIANGLE);

    private WorldRenderer renderer;

    public MotionExample(int width, int height) {
        super(width, height);
    }

    public static void main(final String[] argv) {
        MotionExample displayExample = new MotionExample(WIDTH, HEIGHT);
        try {
            displayExample.start();
        } catch (IOException | LWJGLException e) {
            e.printStackTrace();
        }
    }

    public void initialize() throws IOException {
        Program program = ProgramFactory.build("simple.vert", "simple.frag");
        Camera camera = new Camera(FRUSTUM_SCALE, Z_NEAR, Z_FAR, ASPECT_RATIO);
        renderer = new WorldRenderer(WORLD, program, camera);
    }

    public void draw() {
        double time = getElapsedTime();
        double frequency = 2.0 * Math.PI / PERIOD;
        double xOffset = 0.5f * Math.cos(frequency * time);
        double yOffset = 0.5f * Math.sin(frequency * time);
        TRIANGLE.setOffset(xOffset, yOffset, -1);
        renderer.draw();
    }

    private double getElapsedTime() {
        return (System.currentTimeMillis() - START_TIME) * 0.001;
    }
}
