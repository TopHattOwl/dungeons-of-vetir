package com.tophattowl.dungeonsofvetir.display.ui.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tophattowl.dungeonsofvetir.util.DebugConsole;

public class DebugConsoleRenderer {

    private static final int CONSOLE_H = 300;
    private static final int PADDING = 8;
    private static final int LINE_H = 16;
    private static final Color BG = new Color(0f, 0f, 0f, 0.85f);
    private static final Color BORDER = new Color(0.4f, 0.8f, 0.4f, 1f);
    private static final Color TEXT = new Color(0.9f, 0.9f, 0.9f, 1f);
    private static final Color INPUT_TEXT = new Color(0.4f, 1f, 0.4f, 1f);

    private final DebugConsole console;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final int screenW;
    private final int screenH;

    public DebugConsoleRenderer(int screenH, int screenW, BitmapFont font, DebugConsole console) {
        this.screenH = screenH;
        this.screenW = screenW;
        this.font = font;
        this.shapeRenderer = new ShapeRenderer();
        this.console = console;
    }

//    public void render(SpriteBatch batch) {
//        if (!console.isVisible()) return;
//
//        int consoleY = screenH - CONSOLE_H;
//
//        // Background
//        batch.end();
//        com.badlogic.gdx.Gdx.gl.glEnable(com.badlogic.gdx.graphics.GL20.GL_BLEND);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(BG);
//        shapeRenderer.rect(0, consoleY, screenW, CONSOLE_H);
//        shapeRenderer.end();
//
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.setColor(BORDER);
//        shapeRenderer.rect(0, consoleY, screenW, CONSOLE_H);
//        // Input separator line
//        shapeRenderer.line(0, consoleY + LINE_H + PADDING * 2, screenW, consoleY + LINE_H + PADDING * 2);
//        shapeRenderer.end();
//        com.badlogic.gdx.Gdx.gl.glDisable(com.badlogic.gdx.graphics.GL20.GL_BLEND);
//
//        batch.begin();
//
//        // Input line
//        font.setColor(INPUT_TEXT);
//        font.draw(batch, "> " + console.getInput() + "_",
//            PADDING, consoleY + LINE_H + PADDING);
//
//        // Output lines — draw from bottom up, most recent at bottom
//        font.setColor(TEXT);
//        var lines = console.getOutputLines();
//        int maxLines = (CONSOLE_H - LINE_H - PADDING * 3) / LINE_H;
//        int start = Math.max(0, lines.size() - maxLines);
//
//        for (int i = start; i < lines.size(); i++) {
//            int lineY = consoleY + LINE_H + PADDING * 3 + (i - start) * LINE_H;
//            font.draw(batch, lines.get(i), PADDING, lineY);
//        }
//    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
