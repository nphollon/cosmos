package com.aimlessblade.cosmos.geo;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MovableTest {
    private static final Displacement DISPLACEMENT = Displacement.cartesian(1, 2, 3);
    private static final Orientation ORIENTATION = Orientation.axisAngle(1, 0, 0, 90);
    private static final double TOLERANCE = 1e-5;

    private Movable movable;

    @Before
    public void setup() {
        movable = Movable.createAndPlace(DISPLACEMENT, ORIENTATION);
    }

    @Test
    public void placingAMovableShouldGiveItADisplacementAndOrientation() {
        assertThat(movable.getDisplacement(), is(DISPLACEMENT));
        assertThat(movable.getOrientation(), is(ORIENTATION));
    }

    @Test
    public void movingForwardShouldAdjustZ() {
        Displacement expectedDisplacement = Displacement.cartesian(1, 2, 3.5);

        movable.moveForward(0.5);

        assertThat(movable.getDisplacement(), is(expectedDisplacement));
    }

    @Test
    public void movingUpShouldAdjustY() {
        Displacement expectedDisplacement = Displacement.cartesian(1, 2.5, 3);

        movable.moveUp(0.5);

        assertThat(movable.getDisplacement(), is(expectedDisplacement));
    }

    @Test
    public void movingLeftShouldAdjustX() {
        Displacement expectedDisplacement = Displacement.cartesian(1.5, 2, 3);

        movable.moveLeft(0.5);

        assertThat(movable.getDisplacement(), is(expectedDisplacement));
    }

    @Test
    public void changingPitchShouldRotateAroundXAxis() {
        Orientation expectedOrientation = Orientation.axisAngle(1, 0, 0, 120);

        movable.adjustPitch(30.0);

        assertQuaternionEquality(movable.getOrientation(), expectedOrientation);
    }

    @Test
    public void changingYawShouldRotateAroundYAxis() {
        Orientation expectedOrientation = Orientation.axisAngle(0, 0, 1, 90).times(Orientation.axisAngle(1, 0, 0, 90)).normalize(1.e-5);

        movable.adjustYaw(90.0);

        assertQuaternionEquality(movable.getOrientation(), expectedOrientation);
    }

    @Test
    public void changingRollShouldRotateAroundZAxis() {
        Orientation expectedOrientation = Orientation.axisAngle(0, 1, 0, -90).times(Orientation.axisAngle(1, 0, 0, 90));

        movable.adjustRoll(90.0);

        assertQuaternionEquality(movable.getOrientation(), expectedOrientation);
    }


    private void assertQuaternionEquality(final Orientation quaternion1, final Orientation quaternion2) {
        String message = "\nActual:\n" + quaternion1 + "\nExpected:\n" + quaternion2;
        assertThat(message, quaternion1.isIdentical(quaternion2, TOLERANCE), is(true));
        assertThat(message, quaternion2.isIdentical(quaternion1, TOLERANCE), is(true));
    }
}
