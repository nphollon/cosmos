package com.aimlessblade.cosmos.geo;

import com.aimlessblade.cosmos.app.Processor;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MotionProcessor implements Processor {
    private final List<Movable> movables;

    @Override
    public void run() {

    }
}
