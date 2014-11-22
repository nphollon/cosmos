package com.aimlessblade.cosmos.render;

import com.aimlessblade.cosmos.camera.Camera;
import com.aimlessblade.cosmos.render.DrawData.Target;
import com.aimlessblade.cosmos.physics.Identity;
import org.ejml.simple.SimpleMatrix;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;

import static java.util.Arrays.stream;
import static org.apache.commons.lang.ArrayUtils.addAll;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DrawDataTest {
    private static final double PRECISION = 1e-3;

    private static final double[] MATRIX_DATA = new double[]{
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
    };

    @Mock
    private Camera camera;
    private List<Entity> entities;

    public static void assertBufferContents(final IntBuffer buffer, final int[] expectedData) {
        assertThat(buffer.limit(), is(expectedData.length));
        stream(expectedData).forEach(checkNextBufferEntry(buffer));
    }

    public static DoubleConsumer checkNextBufferEntry(final FloatBuffer buffer, final double tolerance) {
        return d -> assertThat((double) buffer.get(), closeTo(d, tolerance));
    }

    private static IntConsumer checkNextBufferEntry(final IntBuffer buffer) {
        return i -> assertThat(buffer.get(), is(i));
    }

    @Before
    public void setup() {
        entities = new ArrayList<>();
    }

    @Test
    public void shouldPutCameraPerspectiveIntoABuffer() {
        when(camera.getPerspective()).thenReturn(SimpleMatrix.identity(4));

        final DrawData drawData = new DrawData(camera, entities);
        final FloatBuffer perspectiveBuffer = drawData.getPerspective();

        Identity.assertBufferContents(perspectiveBuffer, MATRIX_DATA);
    }

    @Test
    public void vertexBufferShouldBeEmptyIfNoEntities() {
        final DrawData drawData = new DrawData(camera, entities);
        final FloatBuffer vertexBuffer = drawData.getVertexBuffer();
        Identity.assertBufferContents(vertexBuffer, new double[0]);
    }

    @Test
    public void vertexBufferShouldContainDataFromEntity() {
        final double[] expectedVertexData = new double[]{1, 2, 3, 4, 5, 6};
        addEntityWithVertexData(expectedVertexData);

        final DrawData drawData = new DrawData(camera, entities);
        final FloatBuffer vertexBuffer = drawData.getVertexBuffer();

        Identity.assertBufferContents(vertexBuffer, expectedVertexData);
    }

    @Test
    public void vertexBufferShouldContainDataFromAllEntities() {
        final double[] vertexDataA = new double[]{1, 2, 3, 4, 5, 6};
        final double[] vertexDataB = new double[]{36, 25, 16, 9, 4, 1};
        addEntityWithVertexData(vertexDataA);
        addEntityWithVertexData(vertexDataB);

        final DrawData drawData = new DrawData(camera, entities);
        final FloatBuffer vertexBuffer = drawData.getVertexBuffer();

        Identity.assertBufferContents(vertexBuffer, addAll(vertexDataA, vertexDataB));
    }

    @Test
    public void elementBufferShouldBeEmptyIfNoEntities() {
        final DrawData drawData = new DrawData(camera, entities);
        final IntBuffer elementBuffer = drawData.getElementBuffer();
        assertBufferContents(elementBuffer, new int[0]);
    }

    @Test
    public void elementBufferShouldContainDataFromEntity() {
        final int[] expectedElementList = new int[]{1, 2};
        addEntityWithElementData(expectedElementList, 2);

        final DrawData drawData = new DrawData(camera, entities);
        final IntBuffer elementBuffer = drawData.getElementBuffer();

        assertBufferContents(elementBuffer, expectedElementList);
    }

    @Test
    public void elementBufferShouldOffsetIndexesByCumulativeVertexCount() {
        addEntityWithElementData(new int[]{1}, 1);
        addEntityWithElementData(new int[]{2, 1}, 2);

        final int[] expectedContents = new int[]{1, 3, 2};

        final DrawData drawData = new DrawData(camera, entities);
        final IntBuffer elementBuffer = drawData.getElementBuffer();

        assertBufferContents(elementBuffer, expectedContents);
    }

    @Test
    public void elementBufferShouldOffsetIndexesByCumulativeVertexCount2() {
        addEntityWithElementData(new int[]{1, 2}, 2);
        addEntityWithElementData(new int[]{2, 1}, 2);

        final int[] expectedContents = new int[]{1, 2, 4, 3};

        final DrawData drawData = new DrawData(camera, entities);
        final IntBuffer elementBuffer = drawData.getElementBuffer();

        assertBufferContents(elementBuffer, expectedContents);
    }

    @Test
    public void elementBufferShouldOffsetIndexesByCumulativeVertexCount3() {
        addEntityWithElementData(new int[]{1, 2, 1}, 2);
        addEntityWithElementData(new int[]{2, 1}, 2);

        final int[] expectedContents = new int[]{1, 2, 1, 4, 3};

        final DrawData drawData = new DrawData(camera, entities);
        final IntBuffer elementBuffer = drawData.getElementBuffer();

        assertBufferContents(elementBuffer, expectedContents);
    }

    @Test
    public void elementBufferShouldOffsetIndexesByCumulativeVertexCount4() {
        addEntityWithElementData(new int[]{1, 2, 1}, 2);
        addEntityWithElementData(new int[]{1, 4, 3, 2, 3, 1}, 4);
        addEntityWithElementData(new int[]{2, 1}, 2);

        final int[] expectedContents = new int[]{
                1, 2, 1,
                3, 6, 5, 4, 5, 3,
                8, 7
        };


        final DrawData drawData = new DrawData(camera, entities);
        final IntBuffer elementBuffer = drawData.getElementBuffer();

        assertBufferContents(elementBuffer, expectedContents);
    }

    @Test
    public void targetListShouldBeEmptyIfNoEntitiesExist() {
        final DrawData drawData = new DrawData(camera, entities);
        assertThat(drawData.getTargets(), empty());
    }

    @Test
    public void targetShouldHaveElementCountForEntity() {
        addEntityWithElementData(new int[]{1, 2, 1}, 2);

        final List<Target> targets = new DrawData(camera, entities).getTargets();
        assertThat(targets, hasSize(1));

        final Target target = targets.get(0);
        assertThat(target.getElementCount(), is(3));
    }

    @Test
    public void targetOffsetShouldBeCumulativeElementCount() {
        addEntityWithElementData(new int[]{1, 2, 1}, 2);
        addEntityWithElementData(new int[]{2, 1}, 2);

        final List<Target> targets = new DrawData(camera, entities).getTargets();

        final Target firstTarget = targets.get(0);
        final Target secondTarget = targets.get(1);

        assertThat(firstTarget.getElementOffset(), is(0));
        assertThat(secondTarget.getElementOffset(), is(3));
    }

    @Test
    public void targetGeoTransformShouldContainPoseMatrixData() {
        final Entity entity = mock(Entity.class);
        when(entity.getElementData()).thenReturn(new int[0]);
        when(entity.getTransform()).thenReturn(SimpleMatrix.identity(4));
        entities.add(entity);

        final List<Target> targets = new DrawData(camera, entities).getTargets();
        final Target target = targets.get(0);

        Identity.assertBufferContents(target.getGeoTransform(), MATRIX_DATA);
    }

    @Test
    public void targetListShouldBeCached() {
        final Entity entity = mock(Entity.class);
        when(entity.getElementData()).thenReturn(new int[0]);
        entities.add(entity);

        final DrawData drawData = new DrawData(camera, entities);
        final List<Target> firstTargetList = drawData.getTargets();
        final List<Target> secondTargetList = drawData.getTargets();

        assertThat(firstTargetList, sameInstance(secondTargetList));
    }

    private void addEntityWithElementData(final int[] data, final int vertexCount) {
        final Entity entity = mock(Entity.class);
        when(entity.getElementData()).thenReturn(data);
        when(entity.getVertexCount()).thenReturn(vertexCount);
        entities.add(entity);
    }

    private void addEntityWithVertexData(final double[] data) {
        final Entity entity = mock(Entity.class);
        when(entity.getVertexData()).thenReturn(data);
        entities.add(entity);
    }
}
