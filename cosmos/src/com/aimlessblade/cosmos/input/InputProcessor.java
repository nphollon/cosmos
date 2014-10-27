package com.aimlessblade.cosmos.input;

import com.aimlessblade.cosmos.app.Processor;
import com.aimlessblade.cosmos.geo.Movable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class InputProcessor implements Processor {
    private final Keymap keymap;
    private final List<Movable> movables;

    @Override
    public void run() {

    }
}
