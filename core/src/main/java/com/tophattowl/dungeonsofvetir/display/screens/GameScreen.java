package com.tophattowl.dungeonsofvetir.display.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.tophattowl.dungeonsofvetir.display.camera.CameraController;
import com.tophattowl.dungeonsofvetir.display.renderer.FovOverlayRenderer;
import com.tophattowl.dungeonsofvetir.display.renderer.WorldRenderer;
import com.tophattowl.dungeonsofvetir.display.tilesets.PlaceholderTileset;
import com.tophattowl.dungeonsofvetir.display.tilesets.Tileset;
import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.FovComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.PlayerComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.ECS.systems.FovSystem;
import com.tophattowl.dungeonsofvetir.game.input.InputHandler;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.action.ActionHandler;
import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.turn_system.TimeTurnManager;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Level;
import com.tophattowl.dungeonsofvetir.game.world.TileType;

public class GameScreen implements Screen {
    // Layout constants
    private static final int WIN_W       = 1280;
    private static final int WIN_H       = 800;
    private static final int TOP_BAR_H   = 32;
    private static final int SIDE_W      = 256;
    private static final int BOTTOM_H    = 96;
    public  static final int VIEWPORT_W  = WIN_W - SIDE_W;   // 1024
    public  static final int VIEWPORT_H  = WIN_H - TOP_BAR_H - BOTTOM_H; // 672
    public  static final int VIEWPORT_X  = 0;
    public  static final int VIEWPORT_Y  = BOTTOM_H;         // 96

    public  static final int ACTOR_PROCESS_COUNT = 10;

    // display
    private SpriteBatch batch;
    private BitmapFont font;
    private Tileset tileset;
    private WorldRenderer worldRenderer;
    private FovOverlayRenderer fovOverlayRenderer;
    private CameraController cameraController;

    // game
    private GameWorld gameWorld;
    private ActionHandler actionHandler;
    private InputHandler inputHandler;
    private TimeTurnManager timeTurnManager;
    private FovSystem fovSystem;

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        tileset = new PlaceholderTileset();

        cameraController = new CameraController(VIEWPORT_W, VIEWPORT_H);
        worldRenderer = new WorldRenderer(batch, tileset);
        fovOverlayRenderer = new FovOverlayRenderer();
        worldRenderer.setFovOverlayRenderer(fovOverlayRenderer);

        timeTurnManager = new TimeTurnManager();
        gameWorld = new GameWorld();
        actionHandler = new ActionHandler(gameWorld);
        fovSystem = new FovSystem();

        spawnPlayer();

        fovSystem.process(gameWorld);

        FovComponent fov = gameWorld.getPlayer().getComponent(FovComponent.class);
        PositionComponent pos = gameWorld.getPlayer().getComponent(PositionComponent.class);
        int visibleCount = 0;
        for (int x = 0; x < Level.WIDTH; x++)
            for (int y = 0; y < Level.HEIGHT; y++)
                if (fov.visibleTiles[x][y]) visibleCount++;

        inputHandler = new InputHandler(gameWorld.getPlayer());
        Gdx.input.setInputProcessor(inputHandler);
    }

    private void spawnPlayer() {
        Level level = gameWorld.getCurrentLevel();
        int[] spawn = findSpawn(level);

        PositionComponent pos = gameWorld.getPlayer().getComponent(PositionComponent.class);
        pos.set(spawn[0], spawn[1]);

        cameraController.centerOn(spawn[0], spawn[1]);
    }

    private int[] findSpawn(Level level) {
        // Try stairs_up first
        for (int x = 0; x < Level.WIDTH; x++)
            for (int y = 0; y < Level.HEIGHT; y++)
                if (level.getTile(x, y).type == TileType.STAIRS_UP)
                    return new int[]{x, y};
        // Fall back to any walkable tile
        for (int x = 1; x < Level.WIDTH - 1; x++)
            for (int y = 1; y < Level.HEIGHT - 1; y++)
                if (level.isWalkable(x, y))
                    return new int[]{x, y};
        return new int[]{1, 1};
    }


    @Override
    public void render(float v) {
        input();
        logic();
        draw();
    }

    private void input() {
        Entity player = gameWorld.getPlayer();
        PlayerComponent playerComp = player.getComponent(PlayerComponent.class);

        if (!playerComp.isPlayersTurn) return;

        Action action = inputHandler.getPendingAction();

        if (action != null) {
            Action actionFinal = actionHandler.processAction(player, action);

            if (actionFinal.isSuccess()) {
                DebugLogger.log(DebugLogger.Category.ACTION, "GameWorld",
                    "Action successful by player\n" + actionFinal
                );
                playerComp.isPlayersTurn = false;
                fovSystem.process(gameWorld);
                PositionComponent posComp = player.getComponent(PositionComponent.class);
                cameraController.centerOn(posComp.getX(), posComp.getY());
                timeTurnManager.onPlayerActionCompleted(gameWorld);
            }
        }

    }

    private void logic() {
        Entity player = gameWorld.getPlayer();
        PlayerComponent playerComp = player.getComponent(PlayerComponent.class);

        if (playerComp.isPlayersTurn) return;

        // process several actors in a frame
        for (int i = 0; i < ACTOR_PROCESS_COUNT; i++) {
            timeTurnManager.processNext(gameWorld);
            if (gameWorld.getPlayer().getComponent(PlayerComponent.class).isPlayersTurn) break;
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Set the GL viewport to the game area only
        // this makes the camera render into the correct screen region and not clip it
        HdpiUtils.glViewport(VIEWPORT_X, VIEWPORT_Y, VIEWPORT_W, VIEWPORT_H);

        batch.setProjectionMatrix(cameraController.getCamera().combined);
        batch.begin();
        worldRenderer.render(gameWorld, cameraController.getCamera());
        batch.end();

        fovOverlayRenderer.render(gameWorld, cameraController.getCamera());

        // restore full viewport for HUD rendering
        HdpiUtils.glViewport(0, 0, WIN_W, WIN_H);
        //TODO: render hud
    }

    @Override
    public void resize(int width, int height) {
        cameraController.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        tileset.dispose();
        fovOverlayRenderer.dispose();
        EventBus.clear();
        Gdx.input.setInputProcessor(null);
    }
}
