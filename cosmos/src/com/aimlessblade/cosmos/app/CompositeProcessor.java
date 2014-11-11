package com.aimlessblade.cosmos.app;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
final class CompositeProcessor implements Processor {
    private final List<Processor> stages;

    @Override
    public void run() {
        stages.stream().forEach(Processor::run);
    }
}
