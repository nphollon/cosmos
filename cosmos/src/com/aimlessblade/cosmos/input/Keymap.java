package com.aimlessblade.cosmos.input;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.aimlessblade.cosmos.input.InputState.angularImpulse;
import static com.aimlessblade.cosmos.input.InputState.impulse;
import static com.aimlessblade.cosmos.input.InputState.setActiveIndex;
import static com.aimlessblade.cosmos.physics.Vectors.velocity;
import static org.lwjgl.input.Keyboard.*;

public class Keymap {
    private final Map<KeyboardEvent, Consumer<InputState>> keymap;

    public static Map<KeyboardEvent, Consumer<InputState>> standard(double v, double w) {
        final Consumer<InputState> west = impulse(velocity(-v, 0, 0));
        final Consumer<InputState> east = impulse(velocity(v, 0, 0));
        final Consumer<InputState> up = impulse(velocity(0, v, 0));
        final Consumer<InputState> down = impulse(velocity(0, -v, 0));
        final Consumer<InputState> north = impulse(velocity(0, 0, -v));
        final Consumer<InputState> south = impulse(velocity(0, 0, v));

        final Consumer<InputState> turnLeft = angularImpulse(velocity(0, -w, 0));
        final Consumer<InputState> turnRight = angularImpulse(velocity(0, w, 0));
        final Consumer<InputState> tiltUp = angularImpulse(velocity(-w, 0, 0));
        final Consumer<InputState> tiltDown = angularImpulse(velocity(w, 0, 0));
        final Consumer<InputState> tiltLeft = angularImpulse(velocity(0, 0, w));
        final Consumer<InputState> tiltRight = angularImpulse(velocity(0, 0, -w));

        return new Keymap()
                .toggle(KEY_A, KEY_D, west, east)
                .toggle(KEY_W, KEY_S, up, down)
                .toggle(KEY_Q, KEY_E, north, south)
                .toggle(KEY_J, KEY_L, turnLeft, turnRight)
                .toggle(KEY_I, KEY_K, tiltUp, tiltDown)
                .toggle(KEY_U, KEY_O, tiltLeft, tiltRight)
                .select(KEY_1, setActiveIndex(0))
                .select(KEY_2, setActiveIndex(1))
                .select(KEY_3, setActiveIndex(2))
                .build();
    }

    private Keymap() {
        this.keymap = new HashMap<>();
    }

    private Keymap select(final int keycode, Consumer<InputState> action) {
        keymap.put(KeyboardEvent.press(keycode), action);
        return this;
    }

    private Keymap toggle(final int leftKey, final int rightKey, Consumer<InputState> leftAction, Consumer<InputState> rightAction) {
        keymap.put(KeyboardEvent.press(leftKey), leftAction);
        keymap.put(KeyboardEvent.press(rightKey), rightAction);

        keymap.put(KeyboardEvent.lift(leftKey), rightAction);
        keymap.put(KeyboardEvent.lift(rightKey), leftAction);

        return this;
    }

    private Map<KeyboardEvent, Consumer<InputState>> build() {
        return keymap;
    }
}
