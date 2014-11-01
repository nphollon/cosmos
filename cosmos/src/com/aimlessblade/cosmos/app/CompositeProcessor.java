package com.aimlessblade.cosmos.app;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public final class CompositeProcessor implements Processor {
    private final List<Processor> stages;

    public void run() {
        stages.stream().forEach(Processor::run);
    }
}
