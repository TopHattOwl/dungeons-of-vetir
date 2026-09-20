package com.tophattowl.dungeonsofvetir.display.ui.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tophattowl.dungeonsofvetir.display.theme.Theme;

/**
 * Builds a programmatic Scene2D {@link Skin} for the debug console from the shared
 * game font and the HUD theme colors, generated 1x1 textures are owned by the skin
 */
public final class DebugConsoleSkinFactory {

    private DebugConsoleSkinFactory() {}

    public static Skin create(BitmapFont font) {
        Skin skin = new Skin();

        Drawable panel = solid(skin, "console-panel", Theme.CONSOLE_BG);
        Drawable inputBackground = solid(skin, "console-input-bg", new Color(0f, 0f, 0f, 0.55f));
        Drawable inputBackgroundFocused = solid(skin, "console-input-bg-focused", new Color(0.05f, 0.10f, 0.05f, 0.9f));
        Drawable selection = solid(skin, "console-selection", new Color(0.3f, 0.6f, 0.3f, 0.5f));
        Drawable cursor = solid(skin, "console-cursor", Theme.CONSOLE_INPUT);
        Drawable scrollTrack = solid(skin, "console-scroll-track", new Color(0f, 0f, 0f, 0.25f));
        Drawable scrollKnob = solid(skin, "console-scroll-knob", Theme.CONSOLE_BORDER);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Theme.CONSOLE_TEXT);
        skin.add("console", labelStyle);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Theme.CONSOLE_INPUT;
        textFieldStyle.background = inputBackground;
        textFieldStyle.focusedBackground = inputBackgroundFocused;
        textFieldStyle.cursor = cursor;
        textFieldStyle.selection = selection;
        skin.add("console", textFieldStyle);

        ScrollPane.ScrollPaneStyle scrollStyle = new ScrollPane.ScrollPaneStyle();
        scrollStyle.vScroll = scrollTrack;
        scrollStyle.vScrollKnob = scrollKnob;
        skin.add("default", scrollStyle);

        // referenced by the view for its own background
        skin.add("console-panel-bg", panel, Drawable.class);

        return skin;
    }

    private static Drawable solid(Skin skin, String name, Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        Texture texture = new Texture(pixmap);
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        pixmap.dispose();

        skin.add(name + "-texture", texture, Texture.class);

        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
        skin.add(name, drawable, Drawable.class);
        return drawable;
    }
}
