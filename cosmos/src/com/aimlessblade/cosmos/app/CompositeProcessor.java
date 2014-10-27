package com.aimlessblade.cosmos.app;

import lombok.Getter;

import java.util.List;

@Getter
public class CompositeProcessor implements Processor {
    private final List<Processor> stages;

    public CompositeProcessor(final List<Processor> stages) {
        this.stages = stages;
    }

    public void run() {
        stages.stream().forEach(Processor::run);
    }
}
