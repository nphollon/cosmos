package com.aimlessblade.cosmos.app;

import com.aimlessblade.cosmos.geo.Camera;
import com.aimlessblade.cosmos.geo.Entity;
import com.aimlessblade.cosmos.geo.MotionProcessor;
import com.aimlessblade.cosmos.geo.Movable;
import com.aimlessblade.cosmos.input.InputProcessor;
import com.aimlessblade.cosmos.input.Keymap;
import com.aimlessblade.cosmos.render.DrawingProcessor;
import com.aimlessblade.cosmos.render.Program;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class PuppeteerProcessorFactory implements ProcessorFactory {
    private final Keymap keymap;
    private final Program program;

    @Override
    public Processor build(final Camera camera, final List<Entity> entities) {
        List<Movable> movables = new ArrayList<>();
        movables.add(camera);
        movables.addAll(entities);

        Processor inputStage = new InputProcessor(keymap, movables);
        Processor motionStage = new MotionProcessor(movables);
        Processor drawingStage = new DrawingProcessor(program, camera, entities);

        List<Processor> stages = Arrays.asList(inputStage, motionStage, drawingStage);
        return new CompositeProcessor(stages);
    }
}
