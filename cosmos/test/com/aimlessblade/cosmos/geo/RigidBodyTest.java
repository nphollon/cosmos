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

    @Mock private Pose pose;

    private RigidBody body;

    @Before
    public void setup() {
        body = new RigidBody(pose, VERTICES, DRAW_ORDER);
    }

    @Test
    public void geoTransformShouldComeFromPose() {
        when(pose.toMatrix()).thenReturn(MATRIX);

        final SimpleMatrix geoTransform = body.getGeoTransform();

        assertMatrixEquality(geoTransform, MATRIX, TOLERANCE);
    }

    @Test
    public void impulseShouldPassToPose() {
        body.impulse(5, 6, 7);

        verify(pose).impulse(5, 6, 7);
    }

    @Test
    public void evolveShouldPassToPose() {
        body.evolve(8);

        verify(pose).evolve(8);
    }

    @Test
    public void angularImpulseShouldPassToPose() {
        body.angularImpulse(9, 10, 11);

        verify(pose).angularImpulse(9, 10, 11);
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
        final double[] expectedData = new double[] {
                17, 18, 19, 20, 21, 22,
                23, 24, 25, 26, 27, 28
        };

        assertThat(body.getVertexData(), is(expectedData));
    }
}