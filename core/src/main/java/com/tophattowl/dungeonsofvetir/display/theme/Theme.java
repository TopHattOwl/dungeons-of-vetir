package com.tophattowl.dungeonsofvetir.display.theme;

import com.badlogic.gdx.graphics.Color;

public class Theme {

    // Console/Debug colors
    public static final Color CONSOLE_BG = new Color(0f, 0f, 0f, 0.85f);
    public static final Color CONSOLE_BORDER = new Color(0.4f, 0.8f, 0.4f, 1f);
    public static final Color CONSOLE_TEXT = new Color(0.9f, 0.9f, 0.9f, 1f);
    public static final Color CONSOLE_INPUT = new Color(0.4f, 1f, 0.4f, 1f);

    // HUD colors
    public static final Color HUD_BG = new Color(0f, 0f, 0f, 0.85f);
    public static final Color HUD_BORDER = new Color(0.4f, 0.8f, 0.4f, 1f);
    public static final Color HUD_TEXT = new Color(0.5f, 1f, 0.5f, 1f);
    public static final Color HUD_TITLE = new Color(0.5f, 1f, 0.5f, 1f);

    // HP bar colors by body part status
    public static final Color HP_HEALTHY = new Color(0.3f, 0.9f, 0.3f, 1f);
    public static final Color HP_INJURED = new Color(0.9f, 0.9f, 0.3f, 1f);
    public static final Color HP_CRIPPLED = new Color(0.9f, 0.3f, 0.3f, 1f);
    public static final Color HP_DESTROYED = new Color(0.4f, 0.4f, 0.4f, 1f);

    private Theme() {}
}
