package com.aimlessblade.cosmos.render;

import com.aimlessblade.cosmos.app.Processor;
import com.aimlessblade.cosmos.geo.Camera;
import com.aimlessblade.cosmos.geo.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public final class DrawingProcessor implements Processor {
    private final Program program;
    private final Camera camera;
    private final List<Entity> entities;

    @Override
    public void run() {

    }
}
