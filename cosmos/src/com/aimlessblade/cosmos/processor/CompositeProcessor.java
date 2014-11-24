package com.aimlessblade.cosmos.processor;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public final class CompositeProcessor implements Processor {
    private final List<Processor> stages;

    @Override
    public void run() {
        stages.stream().forEach(Processor::run);
    }
}
