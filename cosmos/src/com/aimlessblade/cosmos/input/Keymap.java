package com.aimlessblade.cosmos.input;

import java.util.function.Function;

public interface Keymap {
    public Function<InputState, InputState> getCommand(final int key, final boolean isKeyDown);
}
