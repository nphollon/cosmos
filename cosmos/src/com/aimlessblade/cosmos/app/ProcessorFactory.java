package com.aimlessblade.cosmos.app;

import com.aimlessblade.cosmos.geo.Camera;
import com.aimlessblade.cosmos.geo.Entity;

import java.util.List;

public interface ProcessorFactory {
    Processor build(Camera camera, List<Entity> entities);
}
