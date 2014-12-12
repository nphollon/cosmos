package com.aimlessblade.cosmos.app;

import com.aimlessblade.cosmos.graphics.RigidBody;
import com.aimlessblade.cosmos.graphics.camera.Camera;
import com.aimlessblade.cosmos.graphics.camera.MovableCamera;
import com.aimlessblade.cosmos.graphics.camera.PerspectiveCamera;
import com.aimlessblade.cosmos.graphics.engine.DrawingProcessor;
import com.aimlessblade.cosmos.graphics.vertex.VertexType;
import com.aimlessblade.cosmos.graphics.vertex.io.PLYEntityFactory;
import com.aimlessblade.cosmos.graphics.vertex.io.PLYParseError;
import com.aimlessblade.cosmos.input.InputProcessor;
import com.aimlessblade.cosmos.input.InputState;
import com.aimlessblade.cosmos.input.KeyboardEvent;
import com.aimlessblade.cosmos.input.Keymap;
import com.aimlessblade.cosmos.physics.MotionProcessor;
import com.aimlessblade.cosmos.physics.Movable;
import com.aimlessblade.cosmos.processor.CompositeProcessor;
import com.aimlessblade.cosmos.processor.Processor;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.aimlessblade.cosmos.physics.Vectors.inversePose;
import static com.aimlessblade.cosmos.physics.Vectors.pose;

@AllArgsConstructor
final class ProcessorFactory {
    static Processor build() throws IOException, PLYParseError {
        Map<KeyboardEvent, Consumer<InputState>> keymap = Keymap.standard(5, 3);
        File vertexShader = new File("/home/nick/IdeaProjects/cosmos/cosmos/shaders/simple.vert");
        File fragmentShader = new File("/home/nick/IdeaProjects/cosmos/cosmos/shaders/simple.frag");

        final Camera camera = new PerspectiveCamera(1.0, 1, 50.0, 1.5);
        final MovableCamera movableCamera = new MovableCamera(camera, inversePose(0, 0, 0, 0, 0, 0));

        final Movable pose = pose(0, 0, -6, 0, 0, 0);
        final String fileName = "cosmos/models/simpletetrahedron.ply";
        final RigidBody tetrahedron = new PLYEntityFactory().buildRigidBody(pose, fileName);
        final Movable pose1 = pose(1, 2, -3, 0, 0, 0);
        final RigidBody octahedron = new PLYEntityFactory().buildRigidBody(pose1, "cosmos/models/simpleoctahedron.ply");

        final List<RigidBody> entities = Arrays.asList(octahedron, tetrahedron);
        final VertexType vertexType = entities.get(0).getVertexType();

        final List<Movable> movables = new ArrayList<>();
        movables.addAll(entities);
        movables.add(movableCamera);

        final Processor drawingStage;
        try {
            drawingStage = DrawingProcessor.build(vertexShader, fragmentShader, vertexType, movableCamera, entities);
        } catch (IOException e) {
            throw new ApplicationException("Failed to find shader files.", e);
        }

        final Processor inputStage = new InputProcessor(keymap, movables);
        final Processor motionStage = new MotionProcessor(movables);

        return new CompositeProcessor(Arrays.asList(inputStage, motionStage, drawingStage));
    }
}
