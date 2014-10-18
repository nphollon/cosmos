package com.aimlessblade.test3d;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Color {
    public static final Color BLUE = new Color(0.0, 0.0, 1.0);
    public static final Color GRAY = new Color(0.8, 0.8, 0.8);
    public static final Color GREEN = new Color(0.0, 1.0, 0.0);
    public static final Color YELLOW = new Color(0.5, 0.5, 0.0);
    public static final Color RED = new Color(1.0, 0.0, 0.0);
    public static final Color CYAN = new Color(0.0, 1.0, 1.0);
    
    private final double red;
    private final double green;
    private final double blue;
}
