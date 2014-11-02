package com.aimlessblade.cosmos.input;

import com.aimlessblade.cosmos.app.Processor;
import com.aimlessblade.cosmos.geo.Movable;
import lombok.Getter;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.lwjgl.input.Keyboard.getEventKey;
import static org.lwjgl.input.Keyboard.getEventKeyState;

@Getter
public final class InputProcessor implements Processor {
    private final Map<KeyboardEvent, Function<InputState, InputState>> keymap;
    private final List<Movable> movables;
    private InputState state;

    public InputProcessor(final Map<KeyboardEvent, Function<InputState, InputState>> keymap, final List<Movable> movables) {
        this.keymap = keymap;
        this.movables = movables;
        state = new InputState(movables, 0);
    }

    @Override
    public void run() {
        while (Keyboard.next()) {
            KeyboardEvent event = new KeyboardEvent(getEventKey(), getEventKeyState());
            if (keymap.containsKey(event)) {
                state = keymap.get(event).apply(state);
            }
        }
    }
}
