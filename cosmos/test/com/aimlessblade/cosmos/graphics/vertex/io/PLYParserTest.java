package com.aimlessblade.cosmos.graphics.vertex.io;

import com.aimlessblade.cosmos.graphics.Entity;
import com.aimlessblade.cosmos.graphics.vertex.AttributeType;
import com.aimlessblade.cosmos.graphics.vertex.VertexType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class PLYParserTest {
    private static final VertexType VERTEX_TYPE =
            new VertexType(new AttributeType("position", 3), new AttributeType("color", 3));

    @Mock
    private BufferedReader reader;

    private PLYParser parser;

    @Before
    public void setup() {
        parser = new PLYParser();
    }

    @Test
    public void readHeaderShouldParseFaceAndVertexCountsFromStream() throws Exception {
        when(reader.readLine()).thenReturn(
                "ply",
                "format ascii 1.0",
                "element vertex 10",
                "property float x",
                "property float y",
                "property float z",
                "property float r",
                "property float g",
                "property float b",
                "element face 23",
                "property list uchar int vertex_index",
                "end_header",
                null
        );

        final PLYHeader header = parser.readHeader(reader);

        assertThat(header.getVertexCount(), is(10));
        assertThat(header.getFaceCount(), is(23));
        assertThat(header.getVertexType(), is(VERTEX_TYPE));
    }

    @Test
    public void entityShouldHaveNoDataIfHeaderCountsAreZero() throws Exception {

        final Entity entity = parser.buildEntityFromBody(reader, header(0, 0));

        assertThat(entity.getVertexCount(), is(0));
        assertThat(entity.getVertexData(), empty());
        assertThat(entity.getElementData(), empty());
    }

    @Test
    public void entityShouldHaveOneVertexIfOneLineScannedFromReader() throws Exception {

        when(reader.readLine()).thenReturn("2 4 -6 8 10 0.5", (String) null);

        final Entity entity = parser.buildEntityFromBody(reader, header(1, 0));

        assertThat(entity.getVertexCount(), is(1));
        assertThat(entity.getVertexData(), is(Arrays.asList(2., 4., -6., 8., 10., 0.5)));
    }

    @Test
    public void entityShouldHaveSixElementsIfTwoFacesScannedFromReader() throws Exception {

        when(reader.readLine()).thenReturn(
                "1 2 3 4 5 6",
                "7 8 9 10 11 12",
                "13 14 15 16 17 18",
                "3 2 0 1",
                "3 2 1 0",
                null
        );

        final Entity entity = parser.buildEntityFromBody(reader, header(3, 2));

        assertThat(entity.getElementData(), is(Arrays.asList(2, 0, 1, 2, 1, 0)));
    }

    @Test
    public void entityShouldHaveVertexTypeOfHeader() throws Exception {
        final Entity entity = parser.buildEntityFromBody(reader, header(0, 0));

        assertThat(entity.getVertexType(), is(VERTEX_TYPE));
    }

    @Test(expected = PLYParseError.class)
    public void shouldThrowErrorIfNotEnoughVertexLines() throws Exception {

        when(reader.readLine()).thenReturn("2 4 6 8 10 0.5", (String) null);

        parser.buildEntityFromBody(reader, header(2, 0));
    }

    @Test(expected = PLYParseError.class)
    public void shouldTransformIOExceptionIntoPLYParseError() throws Exception {

        when(reader.readLine()).thenThrow(IOException.class);

        parser.buildEntityFromBody(reader, header(2, 0));
    }

    @Test(expected = PLYParseError.class)
    public void shouldThrowErrorIfTooManyVertexProperties() throws Exception {

        when(reader.readLine()).thenReturn("2 4 6 8 10 0.5 3.2", (String) null);

        parser.buildEntityFromBody(reader, header(1, 0));
    }

    @Test(expected = PLYParseError.class)
    public void shouldTransformNumberFormatExceptionIntoPLYParseError() throws Exception {

        when(reader.readLine()).thenReturn("2 4, 6 8 10 0.5", (String) null);

        parser.buildEntityFromBody(reader, header(1, 0));
    }

    @Test(expected = PLYParseError.class)
    public void shouldThrowErrorIfReceivingDoubleWhenExpectingInt() throws Exception {

        when(reader.readLine()).thenReturn(
                "1 2 3 4 5 6",
                "7 8 9 10 11 12",
                "13 14 15 16 17 18",
                "3 2.1 0 1",
                null
        );

        parser.buildEntityFromBody(reader, header(3, 1));
    }

    @Test(expected = PLYParseError.class)
    public void shouldThrowErrorIfMoreThanThreeFaceElements() throws Exception {
        when(reader.readLine()).thenReturn(
                "1 2 3 4 5 6",
                "7 8 9 10 11 12",
                "13 14 15 16 17 18",
                "3 2 0 1 2",
                null
        );

        parser.buildEntityFromBody(reader, header(3, 1));
    }

    @Test(expected = PLYParseError.class)
    public void shouldThrowErrorIfFaceElementCountIsNotThree() throws Exception {
        when(reader.readLine()).thenReturn(
                "1 2 3 4 5 6",
                "7 8 9 10 11 12",
                "13 14 15 16 17 18",
                "4 2 0 1",
                null
        );

        parser.buildEntityFromBody(reader, header(3, 1));
    }

    private PLYHeader header(final int vertexCount, final int faceCount) {
        return new PLYHeader(vertexCount, faceCount, VERTEX_TYPE);
    }
}