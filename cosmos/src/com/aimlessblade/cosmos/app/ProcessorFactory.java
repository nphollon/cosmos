package com.aimlessblade.cosmos.app;

import com.aimlessblade.cosmos.camera.Camera;
import com.aimlessblade.cosmos.input.InputProcessor;
import com.aimlessblade.cosmos.input.InputState;
import com.aimlessblade.cosmos.input.KeyboardEvent;
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

@AllArgsConstructor
final class ProcessorFactory {
    private final Map<KeyboardEvent, Consumer<InputState>> keymap;
    private final File vertexShader;
    private final File fragmentShader;

    Processor build(final Camera camera, final List<RigidBody> entities) {
        final List<Movable> movables = new ArrayList<>();
        movables.addAll(entities);

        final Processor drawingStage;
        try {
            drawingStage = DrawingProcessor.build(vertexShader, fragmentShader, camera, entities);
        } catch (IOException e) {
            throw new ApplicationException("Failed to find shader files.", e);
        }

        final Processor inputStage = new InputProcessor(keymap, movables);
        final Processor motionStage = new MotionProcessor(movables);

        return new CompositeProcessor(Arrays.asList(inputStage, motionStage, drawingStage));
    }
}
