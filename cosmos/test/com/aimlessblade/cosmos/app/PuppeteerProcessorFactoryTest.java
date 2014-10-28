package com.aimlessblade.cosmos.app;

import com.aimlessblade.cosmos.geo.Camera;
import com.aimlessblade.cosmos.geo.Entity;
import com.aimlessblade.cosmos.geo.MotionProcessor;
import com.aimlessblade.cosmos.geo.Movable;
import com.aimlessblade.cosmos.input.InputProcessor;
import com.aimlessblade.cosmos.input.Keymap;
import com.aimlessblade.cosmos.render.DrawingProcessor;
import com.aimlessblade.cosmos.render.Program;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PuppeteerProcessorFactoryTest {
    @Mock private Keymap keymap;
    @Mock private Program program;
    @Mock private Camera camera;
    @Mock private Entity entity;

    @InjectMocks private PuppeteerProcessorFactory factory;

    private List<Entity> entities;
    private List<Movable> movables;
    private CompositeProcessor pipeline;

    @Before
    public void setup() {
        entities = Collections.singletonList(entity);
        movables = Arrays.asList(camera, entity);
        pipeline = (CompositeProcessor) factory.build(camera, entities);
    }

    @Test
    public void pipelineShouldContainInputProcessorWithKeymap() {
        InputProcessor inputProcessor = getProcessor(0, InputProcessor.class);

        assertThat(inputProcessor.getKeymap(), is(keymap));
        assertThat(inputProcessor.getMovables(), is(movables));
    }

    @Test
    public void pipelineShouldContainMotionProcessor() {
        MotionProcessor motionProcessor = getProcessor(1, MotionProcessor.class);

        assertThat(motionProcessor.getMovables(), is(movables));
    }

    @Test
    public void pipelineShouldContainDrawingProcessor() {
        DrawingProcessor drawingProcessor = getProcessor(2, DrawingProcessor.class);

        assertThat(drawingProcessor.getProgram(), is(program));
        assertThat(drawingProcessor.getCamera(), is(camera));
        assertThat(drawingProcessor.getEntities(), is(entities));
    }

    private <T extends Processor> T getProcessor(int index, Class<T> type) {
        Processor processor = pipeline.getStages().get(index);
        assertThat("Expected " + processor + "to be instance of " + type, type.isInstance(processor), is(true));
        return type.cast(processor);
    }
}