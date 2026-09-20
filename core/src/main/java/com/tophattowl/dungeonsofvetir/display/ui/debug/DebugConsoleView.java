package com.tophattowl.dungeonsofvetir.display.ui.debug;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.tophattowl.dungeonsofvetir.game.debug.DebugConsole;
import com.tophattowl.dungeonsofvetir.game.event.EventSubscriptions;
import com.tophattowl.dungeonsofvetir.game.event.events.input.ConsoleActiveChangedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.input.ConsoleHistoryRequestedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.input.ConsoleSubmitRequestedEvent;

/**
 * Scene2D view for the debug console: scrollable output above a single-line input
 * <p>
 * The command model lives in {@link DebugConsole}
 * this view only renders and forwards
 * input, and never writes to the model's state directly
 */
public class DebugConsoleView extends Table {

    private static final float PANEL_W = 1000f;
    private static final float PANEL_H = 500f;
    private static final float PADDING = 6f;

    private final DebugConsole console;
    private final Label output;
    private final ScrollPane scroll;
    private final TextField input;
    private final EventSubscriptions eventSubs = new EventSubscriptions();

    public DebugConsoleView(DebugConsole console, Skin skin) {
        this.console = console;

        setBackground(skin.getDrawable("console-panel-bg"));
        setTouchable(Touchable.enabled);

        output = new Label("", skin, "console");
        output.setWrap(true);
        output.setAlignment(Align.topLeft);

        scroll = new ScrollPane(output, skin);
        scroll.setScrollingDisabled(true, false);
        scroll.setFadeScrollBars(false);
        scroll.setOverscroll(false, false);

        input = new TextField("", skin, "console");
        input.setFocusTraversal(false);
        // the toggle key must never end up in the input, even if focus lands mid-keypress
        input.setTextFieldFilter((textField, c) -> c != '`' && c != '~');

        add(scroll).grow().pad(PADDING).row();
        add(input).growX().pad(PADDING);

        setSize(PANEL_W, PANEL_H);

        eventSubs.on(ConsoleActiveChangedEvent.class, e -> {
            if (e.active()) refresh();
        });
        eventSubs.on(ConsoleHistoryRequestedEvent.class, e -> navigateHistory(e.direction()));
        eventSubs.on(ConsoleSubmitRequestedEvent.class, e -> submit());
    }

    public TextField getInputField() {
        return input;
    }

    /**
     * Rebuilds the output text and pins the scroll to the newest line (bottom).
     */
    public void refresh() {
        output.setText(String.join("\n", console.getOutputLines()));
        scroll.layout();
        scroll.setScrollPercentY(1f);
        scroll.updateVisualScroll();
    }

    public void focusInput() {
        input.setCursorPosition(input.getText().length());
    }

    private void navigateHistory(int direction) {
        String value = console.navigateHistory(direction, input.getText());
        if (value != null) {
            input.setText(value);
            input.setCursorPosition(value.length());
        }
    }

    private void submit() {
        console.submit(input.getText());
        input.setText("");
        refresh();
    }

    public void dispose() {
        eventSubs.unsubscribeAll();
    }
}
