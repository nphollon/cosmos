package com.aimlessblade.test3d;

import org.lwjgl.LWJGLException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class MotionExample extends DisplayFramework {

    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final float ASPECT_RATIO = 1.0f;
    private static final float FRUSTUM_SCALE = 1;
    private static final float Z_NEAR = 1;
    private static final float Z_FAR = 3;

    private static final long START_TIME = System.currentTimeMillis();
    private static final double PERIOD = 5.0;
    private static final double FREQUENCY = 2.0 * Math.PI / PERIOD;

    private static final List<Vertex> TRIANGLE_VERTICES = Arrays.asList(
            new Vertex(new Position(0, 0.5f, 0), Color.BLUE),
            new Vertex(new Position(0.5f, -0.366f, 0), Color.RED),
            new Vertex(new Position(-0.5f, -0.366f, 0), Color.GREEN)
    );

    private static final List<Integer> TRIANGLE_ELEMENTS = Arrays.asList(0, 1, 2);
    private static final DrawableObject TRIANGLE = new DrawableObject(TRIANGLE_VERTICES, TRIANGLE_ELEMENTS);

    private static final World WORLD = new World(TRIANGLE);
    private WorldRenderer renderer;
    private double lastFrameTime;

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
        TRIANGLE.nudge(0, 0.5, -1);
        lastFrameTime = 0;
    }

    public void draw() {
        double time = getElapsedTime();
        double thetaNudge = FREQUENCY * (time - lastFrameTime);
        double lastFrameX = 0.5 * sin(FREQUENCY * lastFrameTime);
        double lastFrameY = 0.5 * cos(FREQUENCY * lastFrameTime);

        double xNudge = lastFrameX * (cos(thetaNudge) - 1) + lastFrameY * sin(thetaNudge);
        double yNudge = lastFrameY * (cos(thetaNudge) - 1) - lastFrameX * sin(thetaNudge);
        TRIANGLE.nudge(xNudge, yNudge, 0);
        renderer.draw();

        lastFrameTime = time;
    }

    @Override
    public void event() {

    }

    private double getElapsedTime() {
        return (System.currentTimeMillis() - START_TIME) * 0.001;
    }
}
