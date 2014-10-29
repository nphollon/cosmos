package com.aimlessblade.cosmos.input;

import com.aimlessblade.cosmos.app.Processor;
import com.aimlessblade.cosmos.geo.Movable;
import lombok.Getter;
import org.lwjgl.input.Keyboard;

import java.util.List;

@Getter
public final class InputProcessor implements Processor {
    private final Keymap keymap;
    private final List<Movable> movables;
    private InputState state;

    public InputProcessor(final Keymap keymap, final List<Movable> movables) {
        this.keymap = keymap;
        this.movables = movables;
        state = new InputState(movables, 0);
    }

    @Override
    public void run() {
        while (Keyboard.next()) {
            final int eventKey = Keyboard.getEventKey();
            final boolean isKeyDown = Keyboard.getEventKeyState();
            state = keymap.getCommand(eventKey, isKeyDown).apply(state);
        }
    }
}
