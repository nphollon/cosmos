package com.aimlessblade.cosmos.camera;

import com.aimlessblade.cosmos.physics.Movable;
import com.aimlessblade.cosmos.physics.Vectors;
import com.aimlessblade.cosmos.physics.Velocity;
import org.ejml.simple.SimpleMatrix;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.aimlessblade.cosmos.physics.Identity.assertMatrixEquality;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MovableCameraTest {
    private static final SimpleMatrix POSE_MATRIX = SimpleMatrix.diag(2, 3);
    private static final SimpleMatrix STATIC_PERSPECTIVE = SimpleMatrix.diag(8, 9);
    private static final Velocity VELOCITY = Vectors.velocity(1, 2, 3);

    @Mock
    private Movable pose;

    @Mock
    private Camera staticCamera;
    @InjectMocks
    private MovableCamera movableCamera;

    @Before
    public void setup() {
        when(pose.toMatrix()).thenReturn(POSE_MATRIX);
        when(staticCamera.getPerspective()).thenReturn(STATIC_PERSPECTIVE);
    }

    @Test
    public void shouldGetMatrixFromPose() {
        final SimpleMatrix actualMatrix = movableCamera.toMatrix();

        assertMatrixEquality(actualMatrix, POSE_MATRIX);
    }

    @Test
    public void perspectiveShouldBeStaticPerspectiveTimesPose() {
        final SimpleMatrix expectedPerspective = STATIC_PERSPECTIVE.mult(POSE_MATRIX);

        final SimpleMatrix actualPerspective = movableCamera.getPerspective();

        assertMatrixEquality(actualPerspective, expectedPerspective);
    }

    @Test
    public void shouldSendImpulseToPose() {
        movableCamera.impulse(VELOCITY);

        verify(pose).impulse(VELOCITY);
    }

    @Test
    public void shouldSendAngularImpulseToPose() {
        movableCamera.angularImpulse(VELOCITY);

        verify(pose).angularImpulse(VELOCITY);
    }

    @Test
    public void shouldSendEvolveCommandToPose() {
        movableCamera.evolve(7.5);

        verify(pose).evolve(7.5);
    }
}