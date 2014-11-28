package com.aimlessblade.cosmos.integration;

import com.aimlessblade.cosmos.graphics.Entity;
import com.aimlessblade.cosmos.graphics.vertex.io.PLYEntityFactory;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PLYEntityFactoryIntegrationTest {
    @Test
    public void shouldCreateTriangleEntity() throws FileNotFoundException {
        final Reader reader = new BufferedReader(new FileReader("cosmos/test/com/aimlessblade/cosmos/graphics/vertex/io/triangle.ply"));

        final PLYEntityFactory plyEntityFactory = new PLYEntityFactory();
        Entity entity = plyEntityFactory.buildEntity(reader);

        assertThat(entity.getVertexCount(), is(3));
        assertThat(entity.getElementData(), is(Arrays.asList(0, 1, 2)));

        assertThat(entity.getVertexData(), is(Arrays.asList(
                1., 0., 0., 1., 1., 0.,
                0., 1., 0., 1., 0., 1.,
                -1., -1., 0., 0., 1., 1.)));
    }
}