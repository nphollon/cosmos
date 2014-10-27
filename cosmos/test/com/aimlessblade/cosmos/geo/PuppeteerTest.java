package com.aimlessblade.cosmos.geo;

import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PuppeteerTest {

    private Puppeteer puppeteer;
    private List<Pose> entities;

    @Before
    public void setup() {
        DateTimeUtils.setCurrentMillisFixed(10);

        entities = new ArrayList<>();
        puppeteer = new Puppeteer(entities);
    }

    @After
    public void tearDown() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test
    public void moveShouldSendElapsedTimeToEntity() {
        entities.add(mock(Pose.class));

        DateTimeUtils.setCurrentMillisFixed(25);
        puppeteer.move();

        verify(entities.get(0)).evolve(15);
    }

    @Test
    public void moveShouldSendElapsedTimeToAllEntities() {
        entities.add(mock(Pose.class));
        entities.add(mock(Pose.class));

        DateTimeUtils.setCurrentMillisFixed(30);
        puppeteer.move();

        verify(entities.get(0)).evolve(20);
        verify(entities.get(1)).evolve(20);
    }

    @Test
    public void moveShouldSendTimeSinceLastMovement() {
        DateTimeUtils.setCurrentMillisFixed(110);
        puppeteer.move();

        entities.add(mock((Pose.class)));

        DateTimeUtils.setCurrentMillisFixed(180);
        puppeteer.move();

        verify(entities.get(0)).evolve(70);
    }
}