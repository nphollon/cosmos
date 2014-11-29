package com.aimlessblade.cosmos.graphics.vertex.io;

import com.aimlessblade.cosmos.graphics.vertex.VertexType;
import lombok.Getter;

@Getter
public class PLYHeader {
    private final int vertexCount;
    private final int faceCount;
    private final VertexType vertexType;

    public PLYHeader(final int vertexCount, final int faceCount, final VertexType vertexType) {
        this.vertexCount = vertexCount;
        this.faceCount = faceCount;
        this.vertexType = vertexType;
    }
}
