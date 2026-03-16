package com.tophattowl.dungeonsofvetir.display.ui.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tophattowl.dungeonsofvetir.util.DebugConsole;

public class DebugConsoleRenderer {

    private static final int CONSOLE_W = 1000;
    private static final int CONSOLE_H = 500;
    private static final int PADDING = 8;
    private static final int LINE_H = 16;
    private static final Color BG = new Color(0f, 0f, 0f, 0.85f);
    private static final Color BORDER = new Color(0.4f, 0.8f, 0.4f, 1f);
    private static final Color TEXT = new Color(0.9f, 0.9f, 0.9f, 1f);
    private static final Color INPUT_TEXT = new Color(0.4f, 1f, 0.4f, 1f);

    private DebugConsole console;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final int screenW;
    private final int screenH;

    public DebugConsoleRenderer(int screenW, int screenH, BitmapFont font) {
        this.screenW = screenW;
        this.screenH = screenH;
        this.font = font;
        this.shapeRenderer = new ShapeRenderer();
    }

    public void setDebugConsole(DebugConsole console) {
        this.console = console;
    }

    public void render(SpriteBatch batch) {
        if (!console.isActive()) return;

        int consoleY = (screenH - CONSOLE_H) / 2;
        int consoleX = (screenW - CONSOLE_W) / 2;

        // Background
        batch.end();
        com.badlogic.gdx.Gdx.gl.glEnable(com.badlogic.gdx.graphics.GL20.GL_BLEND);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(BG);
        shapeRenderer.rect(consoleX, consoleY, CONSOLE_W, CONSOLE_H);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(BORDER);
        shapeRenderer.rect(consoleX, consoleY, CONSOLE_W, CONSOLE_H);
        // Input separator line
        shapeRenderer.line(
            consoleX,
            consoleY + LINE_H + PADDING * 2,
            consoleX + CONSOLE_W,
            consoleY + LINE_H + PADDING * 2);
        shapeRenderer.end();
        com.badlogic.gdx.Gdx.gl.glDisable(com.badlogic.gdx.graphics.GL20.GL_BLEND);

        batch.begin();

        // Input line
        font.setColor(INPUT_TEXT);
        font.draw(batch, "> " + console.getInputString(),
            consoleX + PADDING,
            consoleY + PADDING + LINE_H);

        // Output lines — draw from bottom up, most recent at bottom
        font.setColor(TEXT);
        var lines = console.getOutputLines();
        int maxLines = (CONSOLE_H - LINE_H - PADDING * 3) / LINE_H;
        int start = Math.max(0, lines.size() - maxLines);

        for (int i = start; i < lines.size(); i++) {
            int lineY = consoleY + LINE_H + PADDING * 3 + (i - start) * LINE_H;
            font.draw(batch, lines.get(i), PADDING, lineY);
        }
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
