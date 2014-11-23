package com.aimlessblade.cosmos.render;

import com.aimlessblade.cosmos.physics.Movable;
import com.aimlessblade.cosmos.physics.Vectors;
import com.aimlessblade.cosmos.physics.Velocity;
import org.ejml.simple.SimpleMatrix;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.aimlessblade.cosmos.physics.Identity.assertMatrixEquality;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RigidBodyTest {

    private static final SimpleMatrix MATRIX = SimpleMatrix.diag(1, 2, 3, 4);

    @Mock
    private Movable pose;

    @Mock
    private Entity entity;

    @InjectMocks
    private RigidBody body;

    @Test
    public void geoTransformShouldComeFromPose() {
        when(pose.toMatrix()).thenReturn(MATRIX);

        final SimpleMatrix geoTransform = body.toMatrix();

        assertMatrixEquality(geoTransform, MATRIX);
    }

    @Test
    public void impulseShouldPassToPose() {
        final Velocity velocity = Vectors.velocity(0, 0, 0);

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
        Velocity angularVelocity = Vectors.velocity(0, 0, 0);

        body.angularImpulse(angularVelocity);

        verify(pose).angularImpulse(angularVelocity);
    }

    @Test
    public void elementDataShouldComeFromDrawOrder() {
        final int[] drawOrder = new int[]{12, 13, 14, 15, 16};
        when(entity.getElementData()).thenReturn(drawOrder);

        assertThat(body.getElementData(), is(drawOrder));
    }

    @Test
    public void vertexCountShouldBeLengthOfVertexList() {
        when(entity.getVertexCount()).thenReturn(2);

        assertThat(body.getVertexCount(), is(2));
    }

    @Test
    public void vertexDataShouldComeFromVertexList() {
        final double[] vertexData = new double[]{ 17, 18, 19, 20 };
        when(entity.getVertexData()).thenReturn(vertexData);

        assertThat(body.getVertexData(), is(vertexData));
    }
}
