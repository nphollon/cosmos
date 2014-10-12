package com.aimlessblade.test3d;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Color {
    public static final Color BLUE = new Color(0.0f, 0.0f, 1.0f);
    public static final Color GRAY = new Color(0.8f, 0.8f, 0.8f);
    public static final Color GREEN = new Color(0.0f, 1.0f, 0.0f);
    public static final Color YELLOW = new Color(0.5f, 0.5f, 0.0f);
    public static final Color RED = new Color(1.0f, 0.0f, 0.0f);
    public static final Color CYAN = new Color(0.0f, 1.0f, 1.0f);
    
    private final float red;
    private final float green;
    private final float blue;
}
