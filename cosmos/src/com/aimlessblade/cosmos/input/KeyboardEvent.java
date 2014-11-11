package com.aimlessblade.cosmos.input;

import lombok.*;

@Getter(AccessLevel.PACKAGE)
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public final class KeyboardEvent {
    private final int eventKey;
    private final boolean isKeyDown;

    public static KeyboardEvent press(int keycode) {
        return new KeyboardEvent(keycode, true);
    }

    public static KeyboardEvent lift(int keycode) {
        return new KeyboardEvent(keycode, false);
    }
}
