package com.aimlessblade.cosmos.graphics.vertex.io;

import com.aimlessblade.cosmos.graphics.Entity;
import com.aimlessblade.cosmos.graphics.vertex.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class PLYParser {

    private static final String PROPERTY_SEPARATOR = " +";
    private static final int ELEMENT_COUNT = 3;
    private static final int FACE_START = 1;
    private static final int FACE_END = FACE_START + ELEMENT_COUNT;

    public PLYHeader readHeader(final BufferedReader reader) throws PLYParseError {
        try {
            String line = reader.readLine();
            while (!"end_header".equals(line)) {
                line = reader.readLine();
            }
            return new PLYHeader(3, 1, new VertexType(new AttributeType("dummy", 6)));
        } catch (IOException e) {
            throw new PLYParseError(e);
        }
    }

    public Entity buildEntityFromBody(final BufferedReader reader, final PLYHeader header) throws PLYParseError {
        final List<Vertex> vertexList = readVertexList(reader, header);
        final List<Integer> elementData = readElementList(reader, header);
        return new VertexListEntity(vertexList, elementData);
    }

    private List<Vertex> readVertexList(final BufferedReader reader, final PLYHeader header) throws PLYParseError {
        final VertexType vertexType = header.getVertexType();
        final List<Vertex> vertexList = new ArrayList<>();

        for (int v = 0; v < header.getVertexCount(); v++) {
            final String line = readLine(reader);
            vertexList.add(getVertex(vertexType, line));
        }
        return vertexList;
    }

    private Vertex getVertex(final VertexType vertexType, final String line) throws PLYParseError {
        try {
            final List<Double> vertexData = stream(line.split(" +")).map(Double::valueOf).collect(toList());
            return vertexType.build(vertexData);
        } catch (VertexDataException | NumberFormatException e) {
            throw new PLYParseError("Error parsing vertex from line: " + line, e);
        }
    }

    private List<Integer> readElementList(final BufferedReader reader, final PLYHeader header) throws PLYParseError {
        final List<Integer> elementList = new ArrayList<>();

        for (int f = 0; f < header.getFaceCount(); f++) {
            final String line = readLine(reader);
            elementList.addAll(getFaceElements(line));
        }

        return elementList;
    }

    private List<Integer> getFaceElements(final String line) throws PLYParseError {
        final List<Integer> faceData;

        try {
            faceData = stream(line.split(PROPERTY_SEPARATOR)).map(Integer::valueOf).collect(toList());
        } catch (NumberFormatException e) {
            throw new PLYParseError("Error parsing face from line: " + line, e);
        }

        if (!faceIsCorrectSize(faceData)) {
            throw new PLYParseError("Face must have exactly " + ELEMENT_COUNT + " elements.\nLine: " + line);
        }

        return faceData.subList(FACE_START, FACE_END);
    }

    private boolean faceIsCorrectSize(final List<Integer> faceData) {
        return (faceData.size() == FACE_END && faceData.get(0) == ELEMENT_COUNT);
    }

    private String readLine(final BufferedReader reader) throws PLYParseError {
        final String line;

        try {
            line = reader.readLine();
        } catch (IOException e) {
            throw new PLYParseError(e);
        }

        if (line == null) {
            throw new PLYParseError("Reached end of input stream before expected");
        }

        return line;
    }
}
