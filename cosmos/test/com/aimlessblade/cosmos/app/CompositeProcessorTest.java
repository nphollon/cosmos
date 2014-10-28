package com.aimlessblade.cosmos.app;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CompositeProcessorTest {
    private List<Processor> stages;
    private CompositeProcessor compositeProcessor;

    @Before
    public void setup() {
        stages = new ArrayList<>();
        stages.add(mock(Processor.class));
        stages.add(mock(Processor.class));

        compositeProcessor = new CompositeProcessor(stages);
    }

    @Test
    public void pipelineShouldContainStages() {
        assertThat(compositeProcessor.getStages(), is(stages));
    }

    @Test
    public void pipelineShouldExecuteStages() {
        compositeProcessor.run();

        verify(stages.get(0)).run();
        verify(stages.get(1)).run();
    }
}