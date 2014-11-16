package com.aimlessblade.cosmos.app;

import com.aimlessblade.cosmos.camera.Camera;
import com.aimlessblade.cosmos.camera.PerspectiveCamera;
import com.aimlessblade.cosmos.input.InputState;
import com.aimlessblade.cosmos.input.KeyboardEvent;
import com.aimlessblade.cosmos.input.Keymap;
import com.aimlessblade.cosmos.render.RigidBody;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.aimlessblade.cosmos.physics.Vectors.pose;
import static com.aimlessblade.cosmos.render.Bodies.octahedron;
import static com.aimlessblade.cosmos.render.Bodies.tetrahedron;

public final class Application {
    public static void main(final String[] args) {
        final Map<KeyboardEvent, Consumer<InputState>> keymap = Keymap.standard(5, 3);
        final File vertexShader = new File("/home/nick/IdeaProjects/cosmos/cosmos/shaders/simple.vert");
        final File fragmentShader = new File("/home/nick/IdeaProjects/cosmos/cosmos/shaders/simple.frag");
        final ProcessorFactory processor = new ProcessorFactory(keymap, vertexShader, fragmentShader);

        final Camera camera = new PerspectiveCamera(1.0, 1, 50.0, 1.5);

        final RigidBody tetrahedron = tetrahedron(pose(0, 0, -6, 0, 0, 0));
        final RigidBody octahedron = octahedron(pose(1, 2, -3, 0, 0, 0));

        final List<RigidBody> entities = Arrays.asList(octahedron, tetrahedron);

        Window window = new Window(800, 600);
        window.loop(processor.build(camera, entities));
    }

}
