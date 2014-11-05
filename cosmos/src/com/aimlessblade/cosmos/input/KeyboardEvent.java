package com.aimlessblade.cosmos.input;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class KeyboardEvent {
    private final int eventKey;
    private final boolean isKeyDown;

    public static KeyboardEvent press(int keycode) {
        return new KeyboardEvent(keycode, true);
    }

    public static KeyboardEvent lift(int keycode) {
        return new KeyboardEvent(keycode, false);
    }
}
