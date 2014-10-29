package com.aimlessblade.cosmos.app;

import com.aimlessblade.cosmos.geo.*;
import com.aimlessblade.cosmos.input.Keymap;
import com.aimlessblade.cosmos.render.Program;
import org.lwjgl.LWJGLException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Application {
    public static void main(String[] args) {
        final Keymap keymap = null;
        final Program program = null;
        final ProcessorFactory factory = new PuppeteerProcessorFactory(keymap, program);

        final Displacement cameraLocation = Displacement.cartesian(0, 0, 0);
        final Orientation cameraOrientation = Orientation.axisAngle(1, 0, 0, 0);
        final Camera camera = new PerspectiveCamera(cameraLocation, cameraOrientation);

        final List<Entity> entities = new ArrayList<>();

        final DisplayFramework window = new DisplayFramework(800, 600, factory);

        try {
            window.start(camera, entities);
        } catch (IOException | LWJGLException e) {
            e.printStackTrace();
        }
    }
}
