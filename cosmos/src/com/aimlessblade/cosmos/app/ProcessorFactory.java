package com.aimlessblade.cosmos.app;

import com.aimlessblade.cosmos.camera.Camera;
import com.aimlessblade.cosmos.camera.MovableCamera;
import com.aimlessblade.cosmos.camera.PerspectiveCamera;
import com.aimlessblade.cosmos.input.InputProcessor;
import com.aimlessblade.cosmos.input.InputState;
import com.aimlessblade.cosmos.input.KeyboardEvent;
import com.aimlessblade.cosmos.input.Keymap;
import com.aimlessblade.cosmos.physics.MotionProcessor;
import com.aimlessblade.cosmos.physics.Movable;
import com.aimlessblade.cosmos.render.DrawingProcessor;
import com.aimlessblade.cosmos.render.RigidBody;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.aimlessblade.cosmos.physics.Vectors.pose;
import static com.aimlessblade.cosmos.render.Bodies.octahedron;
import static com.aimlessblade.cosmos.render.Bodies.tetrahedron;

@AllArgsConstructor
final class ProcessorFactory {
    static Processor build() {
        Map<KeyboardEvent, Consumer<InputState>> keymap = Keymap.standard(5, 3);
        File vertexShader = new File("/home/nick/IdeaProjects/cosmos/cosmos/shaders/simple.vert");
        File fragmentShader = new File("/home/nick/IdeaProjects/cosmos/cosmos/shaders/simple.frag");

        final Camera camera = new PerspectiveCamera(1.0, 1, 50.0, 1.5);
        final MovableCamera movableCamera = new MovableCamera(camera, pose(0, 0, 0, 0, 0, 0));

        final RigidBody tetrahedron = tetrahedron(pose(0, 0, -6, 0, 0, 0));
        final RigidBody octahedron = octahedron(pose(1, 2, -3, 0, 0, 0));

        final List<RigidBody> entities = Arrays.asList(octahedron, tetrahedron);
        final List<Movable> movables = new ArrayList<>();
        movables.addAll(entities);
        movables.add(movableCamera);

        final Processor drawingStage;
        try {
            drawingStage = DrawingProcessor.build(vertexShader, fragmentShader, movableCamera, entities);
        } catch (IOException e) {
            throw new ApplicationException("Failed to find shader files.", e);
        }

        final Processor inputStage = new InputProcessor(keymap, movables);
        final Processor motionStage = new MotionProcessor(movables);

        return new CompositeProcessor(Arrays.asList(inputStage, motionStage, drawingStage));
    }
}
