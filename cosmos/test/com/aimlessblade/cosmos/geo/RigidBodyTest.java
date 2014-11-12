package com.aimlessblade.cosmos.geo;

import org.ejml.simple.SimpleMatrix;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static com.aimlessblade.cosmos.util.Assert.assertMatrixEquality;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RigidBodyTest {
    private static final SimpleMatrix MATRIX = SimpleMatrix.diag(1, 2, 3, 4);
    private static final int[] DRAW_ORDER = new int[] { 12, 13, 14, 15, 16 };
    private static final List<Vertex> VERTICES = Arrays.asList(
            Vertex.build(17, 18, 19, 20, 21, 22),
            Vertex.build(23, 24, 25, 26, 27, 28 )
    );
    private static final double TOLERANCE = 1e-5;

    @Mock private Movable pose;

    private RigidBody body;

    @Before
    public void setup() {
        body = new RigidBody(pose, VERTICES, DRAW_ORDER);
    }

    @Test
    public void geoTransformShouldComeFromPose() {
        when(pose.toMatrix()).thenReturn(MATRIX);

        final SimpleMatrix geoTransform = body.getTransform();

        assertMatrixEquality(geoTransform, MATRIX, TOLERANCE);
    }

    @Test
    public void impulseShouldPassToPose() {
        final Velocity velocity = Velocity.cartesian(0, 0, 0);

        body.impulse(velocity);

        verify(pose).impulse(velocity);
    }

    @Test
    public void evolveShouldPassToPose() {
        body.evolve(8);

        verify(pose).evolve(8);
    }

    @Test
    public void angularImpulseShouldPassToPose() {
        Velocity angularVelocity = Velocity.cartesian(0, 0, 0);

        body.angularImpulse(angularVelocity);

        verify(pose).angularImpulse(angularVelocity);
    }

    @Test
    public void elementDataShouldComeFromDrawOrder() {
        assertThat(body.getElementData(), is(DRAW_ORDER));
    }

    @Test
    public void vertexCountShouldBeLengthOfVertexList() {
        assertThat(body.getVertexCount(), is(2));
    }

    @Test
    public void vertexDataShouldComeFromVertexList() {
        // This test depends on the behavior of Vertex.data()
        final double[] expectedData = new double[] {
                20, 21, 22, 17, 18, 19,
                26, 27, 28, 23, 24, 25
        };

        assertThat(body.getVertexData(), is(expectedData));
    }
}