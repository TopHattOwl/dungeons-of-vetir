package com.tophattowl.dungeonsofvetir.display.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tophattowl.dungeonsofvetir.display.theme.Theme;
import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.components.BodyComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.IdentityComponent;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.action.AttackAction;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.ActionCompletedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityRemovedEvent;

import java.util.ArrayList;
import java.util.List;

public class HudRenderer {
    public static final int SIDE_W = 256;
    public static final int PADDING = 8;
    public static final int LINE_H = 16;
    public static final int TITLE_SIZE = 14;
    public static final int HP_BAR_W = 80;
    public static final int HP_BAR_H = 10;

    // TODO: replace
    private static final int PLACEHOLDER_MAGIC_NUMBER = 100;

    private final BitmapFont font;
    private final BitmapFont titleFont;
    private final ShapeRenderer shapeRenderer;
    private final int screenW;
    private final int screenH;
    private final int sidebarX;

    private Entity player;
    private Entity targetEntity;

    private final List<EventBus.ListenerHandle<?>> listeners = new ArrayList<>();

    public HudRenderer(int screenW, int screenH, BitmapFont font) {
        this.screenW = screenW;
        this.screenH = screenH;
        this.sidebarX = screenW - SIDE_W;
        this.font = font;
        this.shapeRenderer = new ShapeRenderer();

        this.titleFont = new BitmapFont();
        this.titleFont.getData().setScale(1.2f);

        listeners.add(EventBus.on(ActionCompletedEvent.class, this::onActionCompleted));
        listeners.add(EventBus.on(EntityRemovedEvent.class, this::onEntityRemoved));
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

    private void onActionCompleted(ActionCompletedEvent event) {
        Action action = event.action();
        if (action instanceof AttackAction attackAction) {
            if (attackAction.getOwner() == player) {
                targetEntity = attackAction.getTarget();
            }
        }
    }

    private void onEntityRemoved(EntityRemovedEvent event) {
        if (event.entity() == targetEntity) {
            targetEntity = null;
        }
    }

    public void render(SpriteBatch batch) {
        if (player == null) return;

        batch.end();

        com.badlogic.gdx.Gdx.gl.glEnable(com.badlogic.gdx.graphics.GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Theme.HUD_BG);
        shapeRenderer.rect(sidebarX, 0, SIDE_W, screenH);
        shapeRenderer.end();
        com.badlogic.gdx.Gdx.gl.glDisable(com.badlogic.gdx.graphics.GL20.GL_BLEND);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Theme.HUD_BORDER);
        shapeRenderer.line(sidebarX, 0, sidebarX, screenH);
        shapeRenderer.end();

        batch.begin();

        int y = screenH - PADDING - TITLE_SIZE - PADDING;

        renderPlayer(batch, y);
        y -= PADDING * 2 + TITLE_SIZE + PLACEHOLDER_MAGIC_NUMBER;

        if (targetEntity != null) {
            renderTarget(batch, y);
        }
    }

    private void renderPlayer(SpriteBatch batch, int y) {
        titleFont.setColor(Theme.HUD_TITLE);
        titleFont.draw(batch, "PLAYER", sidebarX + PADDING, y);

        // gap for larger text
        // TODO: do better
        y -= 10;
        BodyComponent bodyComp = player.getComponent(BodyComponent.class);
        if (bodyComp != null) {
            renderBodyParts(batch, bodyComp, y - PADDING);
        }
    }

    private void renderTarget(SpriteBatch batch, int y) {
        String targetName = "TARGET";
        IdentityComponent idComp = targetEntity.getComponent(IdentityComponent.class);
        if (idComp != null) {
            targetName = "TARGET: " + idComp.name;
        }

        titleFont.setColor(Theme.HUD_TITLE);
        titleFont.draw(batch, targetName, sidebarX + PADDING, y);

        // gap for larger text
        // TODO: do better
        y -= 10;
        BodyComponent bodyComp = targetEntity.getComponent(BodyComponent.class);
        if (bodyComp != null) {
            renderBodyParts(batch, bodyComp, y - PADDING);
        }
    }

    private void renderBodyParts(SpriteBatch batch, BodyComponent bodyComp, int startY) {
        int y = startY;
        for (BodyPart part : bodyComp.bodyParts) {
            String name = part.name;
            int hp = part.hp;
            int maxHp = part.maxHp;

            font.setColor(Theme.HUD_TEXT);
            font.draw(batch, name + ":", sidebarX + PADDING, y);

            float hpRatio = (float) hp / maxHp;
            Color hpColor = getHpColor(hpRatio, part.isDestroyed());

            int barX = sidebarX + SIDE_W - PADDING - HP_BAR_W;
            renderHpBar(batch, barX, y - HP_BAR_H + 2, HP_BAR_W, HP_BAR_H, hpRatio, hpColor);

            y -= LINE_H;
        }
    }

    private void renderHpBar(SpriteBatch batch, int x, int y, int w, int h, float ratio, Color color) {
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Theme.HUD_BORDER);
        shapeRenderer.rect(x, y, w, h);
        shapeRenderer.setColor(Theme.HUD_BG);
        shapeRenderer.rect(x + 1, y + 1, w - 2, h - 2);
        if (ratio > 0) {
            shapeRenderer.setColor(color);
            int fillW = (int) ((w - 2) * Math.min(1, ratio));
            if (fillW > 0) {
                shapeRenderer.rect(x + 1, y + 1, fillW, h - 2);
            }
        }
        shapeRenderer.end();

        batch.begin();
    }

    private Color getHpColor(float ratio, boolean destroyed) {
        if (destroyed) return Theme.HP_DESTROYED;
        if (ratio <= 0.3f) return Theme.HP_CRIPPLED;
        if (ratio <= 0.6f) return Theme.HP_INJURED;
        return Theme.HP_HEALTHY;
    }

    public void dispose() {
        titleFont.dispose();
        shapeRenderer.dispose();
        listeners.forEach(EventBus::off);
        listeners.clear();
    }
}
