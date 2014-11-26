package com.aimlessblade.cosmos.graphics.vertex.io;

import com.aimlessblade.cosmos.graphics.Entity;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PLYReaderIntegrationTest {
    @Ignore
    @Test
    public void shouldCreateTriangleEntity() throws FileNotFoundException {
        // Use classpath here?
        final Reader reader = new BufferedReader(new FileReader("/home/nick/IdeaProjects/cosmos/cosmos/test/com/aimlessblade/cosmos/graphics/vertex/io/triangle.ply"));

        final PLYReader plyReader = new PLYReader();
        Entity entity = plyReader.buildEntity(reader);

        assertThat(entity.getVertexCount(), is(3));
        assertThat(entity.getElementData(), is(Arrays.asList(0, 1, 2)));

        assertThat(entity.getVertexData(), is(Arrays.asList(
                1., 0., 0., 1., 1., 0.,
                0., 1., 0., 1., 0., 1.,
                -1., -1., 0., 0., 1., 1.)));
    }
}