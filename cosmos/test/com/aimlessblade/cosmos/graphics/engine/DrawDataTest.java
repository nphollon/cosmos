package com.aimlessblade.cosmos.graphics.engine;

import com.aimlessblade.cosmos.graphics.Entity;
import com.aimlessblade.cosmos.graphics.camera.Camera;
import com.aimlessblade.cosmos.graphics.engine.DrawData.Target;
import org.ejml.simple.SimpleMatrix;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

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
    private static final List<Double> MATRIX_DATA = Arrays.asList(
            1., 0., 0., 0.,
            0., 1., 0., 0.,
            0., 0., 1., 0.,
            0., 0., 0., 1.
    );
    private static final double TOLERANCE = 1e-5;

    @Mock
    private Camera camera;
    private List<Entity> entities;

    @Before
    public void setup() {
        entities = new ArrayList<>();
    }

    @Test
    public void shouldPutCameraPerspectiveIntoABuffer() {
        when(camera.getPerspective()).thenReturn(SimpleMatrix.identity(4));

        final DrawData drawData = new DrawData(camera, entities);
        final FloatBuffer perspectiveBuffer = drawData.getPerspective();

        assertBufferContents(perspectiveBuffer, MATRIX_DATA);
    }

    @Test
    public void vertexBufferShouldBeEmptyIfNoEntities() {
        final DrawData drawData = new DrawData(camera, entities);
        final FloatBuffer vertexBuffer = drawData.getVertexBuffer();
        assertBufferContents(vertexBuffer, new ArrayList<>());
    }

    @Test
    public void vertexBufferShouldContainDataFromEntity() {
        final List<Double> expectedVertexData = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        addEntityWithVertexData(expectedVertexData);

        final DrawData drawData = new DrawData(camera, entities);
        final FloatBuffer vertexBuffer = drawData.getVertexBuffer();

        assertBufferContents(vertexBuffer, expectedVertexData);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void vertexBufferShouldContainDataFromAllEntities() {
        final List<Double> expectedBufferContents = new ArrayList<>();

        final List<Double> vertexDataA = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        addEntityWithVertexData(vertexDataA);
        expectedBufferContents.addAll(vertexDataA);

        final List<Double> vertexDataB = Arrays.asList(36.0, 25.0, 16.0, 9.0, 4.0, 1.0);
        addEntityWithVertexData(vertexDataB);
        expectedBufferContents.addAll(vertexDataB);

        final DrawData drawData = new DrawData(camera, entities);
        final FloatBuffer vertexBuffer = drawData.getVertexBuffer();

        assertBufferContents(vertexBuffer, expectedBufferContents);
    }

    @Test
    public void elementBufferShouldBeEmptyIfNoEntities() {
        final DrawData drawData = new DrawData(camera, entities);
        final IntBuffer elementBuffer = drawData.getElementBuffer();
        assertBufferContents(elementBuffer, new ArrayList<>());
    }

    @Test
    public void elementBufferShouldContainDataFromEntity() {
        final List<Integer> expectedElementList = Arrays.asList(1, 2);
        addEntityWithElementData(expectedElementList, 2);

        final DrawData drawData = new DrawData(camera, entities);
        final IntBuffer elementBuffer = drawData.getElementBuffer();

        assertBufferContents(elementBuffer, expectedElementList);
    }

    @Test
    public void elementBufferShouldOffsetIndexesByCumulativeVertexCount() {
        addEntityWithElementData(Arrays.asList(1), 1);
        addEntityWithElementData(Arrays.asList(2, 1), 2);

        final List<Integer> expectedContents = Arrays.asList(1, 3, 2);

        final DrawData drawData = new DrawData(camera, entities);
        final IntBuffer elementBuffer = drawData.getElementBuffer();

        assertBufferContents(elementBuffer, expectedContents);
    }

    @Test
    public void elementBufferShouldOffsetIndexesByCumulativeVertexCount2() {
        addEntityWithElementData(Arrays.asList(1, 2), 2);
        addEntityWithElementData(Arrays.asList(2, 1), 2);

        final List<Integer> expectedContents = Arrays.asList(1, 2, 4, 3);

        final DrawData drawData = new DrawData(camera, entities);
        final IntBuffer elementBuffer = drawData.getElementBuffer();

        assertBufferContents(elementBuffer, expectedContents);
    }

    @Test
    public void elementBufferShouldOffsetIndexesByCumulativeVertexCount3() {
        addEntityWithElementData(Arrays.asList(1, 2, 1), 2);
        addEntityWithElementData(Arrays.asList(2, 1), 2);

        final List<Integer> expectedContents = Arrays.asList(1, 2, 1, 4, 3);

        final DrawData drawData = new DrawData(camera, entities);
        final IntBuffer elementBuffer = drawData.getElementBuffer();

        assertBufferContents(elementBuffer, expectedContents);
    }

    @Test
    public void elementBufferShouldOffsetIndexesByCumulativeVertexCount4() {
        addEntityWithElementData(Arrays.asList(1, 2, 1), 2);
        addEntityWithElementData(Arrays.asList(1, 4, 3, 2, 3, 1), 4);
        addEntityWithElementData(Arrays.asList(2, 1), 2);

        final List<Integer> expectedContents = Arrays.asList(
                1, 2, 1,
                3, 6, 5, 4, 5, 3,
                8, 7
        );


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
        addEntityWithElementData(Arrays.asList(1, 2, 1), 2);

        final List<Target> targets = new DrawData(camera, entities).getTargets();
        assertThat(targets, hasSize(1));

        final Target target = targets.get(0);
        assertThat(target.getElementCount(), is(3));
    }

    @Test
    public void targetOffsetShouldBeCumulativeElementCount() {
        addEntityWithElementData(Arrays.asList(1, 2, 1), 2);
        addEntityWithElementData(Arrays.asList(2, 1), 2);

        final List<Target> targets = new DrawData(camera, entities).getTargets();

        final Target firstTarget = targets.get(0);
        final Target secondTarget = targets.get(1);

        assertThat(firstTarget.getElementOffset(), is(0));
        assertThat(secondTarget.getElementOffset(), is(3));
    }

    @Test
    public void targetGeoTransformShouldContainPoseMatrixData() {
        final Entity entity = mock(Entity.class);
        when(entity.getElementData()).thenReturn(new ArrayList<>());
        when(entity.toMatrix()).thenReturn(SimpleMatrix.identity(4));
        entities.add(entity);

        final List<Target> targets = new DrawData(camera, entities).getTargets();
        final Target target = targets.get(0);

        assertBufferContents(target.getGeoTransform(), MATRIX_DATA);
    }

    @Test
    public void targetListShouldBeCached() {
        final Entity entity = mock(Entity.class);
        when(entity.getElementData()).thenReturn(new ArrayList<>());
        entities.add(entity);

        final DrawData drawData = new DrawData(camera, entities);
        final List<Target> firstTargetList = drawData.getTargets();
        final List<Target> secondTargetList = drawData.getTargets();

        assertThat(firstTargetList, sameInstance(secondTargetList));
    }

    private void addEntityWithElementData(final List<Integer> data, final int vertexCount) {
        final Entity entity = mock(Entity.class);
        when(entity.getElementData()).thenReturn(data);
        when(entity.getVertexCount()).thenReturn(vertexCount);
        entities.add(entity);
    }

    private void addEntityWithVertexData(final List<Double> data) {
        final Entity entity = mock(Entity.class);
        when(entity.getVertexData()).thenReturn(data);
        entities.add(entity);
    }

    private static void assertBufferContents(final IntBuffer buffer, final List<Integer> expectedData) {
        assertThat(buffer.limit(), is(expectedData.size()));
        expectedData.stream().forEach(checkNextBufferEntry(buffer));
    }

    private static Consumer<Double> checkNextBufferEntry(final FloatBuffer buffer) {
        return d -> assertThat((double) buffer.get(), closeTo(d, TOLERANCE));
    }

    private static Consumer<Integer> checkNextBufferEntry(final IntBuffer buffer) {
        return i -> assertThat(buffer.get(), is(i));
    }

    private static void assertBufferContents(final FloatBuffer buffer, final List<Double> expectedData) {
        assertThat(buffer.limit(), is(expectedData.size()));
        expectedData.stream().forEach(checkNextBufferEntry(buffer));
    }
}
