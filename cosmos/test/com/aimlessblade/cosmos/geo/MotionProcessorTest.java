package com.aimlessblade.cosmos.geo;

import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MotionProcessorTest {
    private MotionProcessor processor;
    private List<Movable> movables;

    @Before
    public void setup() {
        DateTimeUtils.setCurrentMillisFixed(10);

        movables = new ArrayList<>();
        processor = new MotionProcessor(movables);
    }

    @After
    public void tearDown() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test
    public void moveShouldSendElapsedTimeToEntity() {
        movables.add(mock(Movable.class));

        DateTimeUtils.setCurrentMillisFixed(1010);
        processor.run();

        verify(movables.get(0)).evolve(1);
    }

    @Test
    public void moveShouldSendElapsedTimeToAllEntities() {
        movables.add(mock(Movable.class));
        movables.add(mock(Movable.class));

        DateTimeUtils.setCurrentMillisFixed(510);
        processor.run();

        verify(movables.get(0)).evolve(0.5);
        verify(movables.get(1)).evolve(0.5);
    }

    @Test
    public void moveShouldSendTimeSinceLastMovement() {
        DateTimeUtils.setCurrentMillisFixed(110);
        processor.run();

        movables.add(mock((Movable.class)));

        DateTimeUtils.setCurrentMillisFixed(360);
        processor.run();

        verify(movables.get(0)).evolve(0.25);
    }
}