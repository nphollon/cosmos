package com.aimlessblade.cosmos.graphics.vertex.io;

import com.aimlessblade.cosmos.graphics.Entity;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertTrue;

public class PLYReaderTest {
    @Test
    @Ignore
    public void shouldParseTestFile() throws FileNotFoundException {
        BufferedInputStream inStream = new BufferedInputStream(new FileInputStream("test.ply"));
        PLYReader reader = new PLYReader();
        Entity entity = reader.parseEntity(inStream);

        // Who is the source of truth for the vertex attributes?

        // The processor wants lists of doubles and ints
        // But we want lists of vertices and ints

        // How to treat fans and ribbons?
        assertTrue(true);
    }
}