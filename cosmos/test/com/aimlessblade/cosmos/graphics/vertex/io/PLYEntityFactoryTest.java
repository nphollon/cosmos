package com.aimlessblade.cosmos.graphics.vertex.io;

import com.aimlessblade.cosmos.graphics.Entity;
import org.junit.Test;

import java.io.BufferedReader;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PLYEntityFactoryTest {
    @Test
    public void shouldParseHeaderAndBodySeparately() throws Exception {
        final BufferedReader reader = mock(BufferedReader.class);
        final Entity expectedEntity = mock(Entity.class);
        final PLYHeader header = mock(PLYHeader.class);

        final PLYParser parser = mock(PLYParser.class);
        when(parser.readHeader(reader)).thenReturn(header);
        when(parser.buildEntityFromBody(reader, header)).thenReturn(expectedEntity);

        final PLYEntityFactory factory = new PLYEntityFactory(parser);
        final Entity actualEntity = factory.buildEntity(reader);

        assertThat(actualEntity, is(expectedEntity));
    }
}