package com.tophattowl.dungeonsofvetir.display.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.math.Matrix4;
import com.tophattowl.dungeonsofvetir.display.camera.CameraController;
import com.tophattowl.dungeonsofvetir.display.renderer.DijkstraOverlayRenderer;
import com.tophattowl.dungeonsofvetir.display.renderer.FovOverlayRenderer;
import com.tophattowl.dungeonsofvetir.display.renderer.WorldRenderer;
import com.tophattowl.dungeonsofvetir.display.tilesets.PlaceholderTileset;
import com.tophattowl.dungeonsofvetir.display.tilesets.Tileset;
import com.tophattowl.dungeonsofvetir.display.ui.debug.DebugConsoleRenderer;
import com.tophattowl.dungeonsofvetir.display.ui.HudRenderer;
import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.action.ActionHandler;
import com.tophattowl.dungeonsofvetir.game.action.EquipAction;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.components.EquipmentComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.PlayerComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.ECS.systems.FovSystem;
import com.tophattowl.dungeonsofvetir.game.factory.action.ActionFactory;
import com.tophattowl.dungeonsofvetir.game.factory.items.ItemFactory;
import com.tophattowl.dungeonsofvetir.game.input.InputHandler;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType;
import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.game.items.ItemId;
import com.tophattowl.dungeonsofvetir.game.rng.SeedConfig;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Point;
import com.tophattowl.dungeonsofvetir.game.debug.DebugConsole;
import com.tophattowl.dungeonsofvetir.util.dijkstra.DijkstraMapManager;

import java.util.Random;

public class GameScreen implements Screen {
    // Layout constants
    private static final int WIN_W       = 1280;
    private static final int WIN_H       = 800;
    private static final int TOP_BAR_H   = 32;
    private static final int SIDE_W      = HudRenderer.SIDE_W;
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
    private DijkstraOverlayRenderer dijkstraOverlayRenderer;
    private CameraController cameraController;
    private DebugConsoleRenderer  debugConsoleRenderer;
    private HudRenderer hudRenderer;

    // game
    private GameWorld gameWorld;
    private InputHandler inputHandler;
    private FovSystem fovSystem;
    private DebugConsole debugConsole;

    @Override
    public void show() {
        // display
        batch = new SpriteBatch();
        font = new BitmapFont();
        tileset = new PlaceholderTileset();

        cameraController = new CameraController(VIEWPORT_W, VIEWPORT_H);
        worldRenderer = new WorldRenderer(batch, tileset);
        fovOverlayRenderer = new FovOverlayRenderer();
        worldRenderer.setFovOverlayRenderer(fovOverlayRenderer);
        dijkstraOverlayRenderer = new DijkstraOverlayRenderer(batch, font);
        debugConsoleRenderer = new DebugConsoleRenderer(WIN_W, WIN_H, font);
        hudRenderer = new HudRenderer(WIN_W, WIN_H, font);


        // game
        gameWorld = new GameWorld(SeedConfig.custom(178439));
        inputHandler = new InputHandler(gameWorld.getPlayer());
        fovSystem = new FovSystem();
        debugConsole = new DebugConsole();

        debugConsole.setGameWorld(gameWorld);
        dijkstraOverlayRenderer.setGameWorld(gameWorld);
        debugConsole.setDijkstraOverlayRenderer(dijkstraOverlayRenderer);
        debugConsoleRenderer.setDebugConsole(debugConsole);

        hudRenderer.setPlayer(gameWorld.getPlayer());

        fovSystem.process(gameWorld);
        gameWorld.addDijkstraMapManager(new DijkstraMapManager(gameWorld));

        Gdx.input.setInputProcessor(inputHandler);

        Point playerPos = gameWorld.getPlayer().getComponent(PositionComponent.class).getPosition();
        cameraController.centerOn(playerPos.x,  playerPos.y);


        // testing weapon equip
        Entity player = gameWorld.getPlayer();
        Item item = ItemFactory.makeItem(ItemId.STEEL_LONGSWORD);
        Item itemOneHanded = ItemFactory.makeItem(ItemId.STEEL_MACE);
        BodyPart bodyPart = player.getComponent(EquipmentComponent.class).getMainHandSlot().bodyPart;

        Action actionn = ActionHandler.prepareAction(player,
            ActionFactory.createEquipAction(player, item, bodyPart, EquipmentSlotType.HAND_SLOT));
        ActionHandler.executeActionDebug(player, actionn);

        EquipmentComponent ec = player.getComponent(EquipmentComponent.class);
        System.out.println(ec);
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

        // if input made no action it's still players turn
        if (action == null) return;


        Action actionFinal = ActionHandler.prepareAction(player, action);
        if (actionFinal.notPossible()) {
            return;
        }

        Action executedAction = ActionHandler.executeAction(player, actionFinal);

        if (executedAction.isSuccess()) {
            DebugLogger.log(DebugLogger.Category.ACTION, "GameWorld",
                "Action successful by player\n" + actionFinal
            );
            playerComp.isPlayersTurn = false;
            fovSystem.process(gameWorld);
            PositionComponent posComp = player.getComponent(PositionComponent.class);
            cameraController.centerOn(posComp.getX(), posComp.getY());
            gameWorld.timeTurnManager.onPlayerActionCompleted(gameWorld);
        }
    }

    private void logic() {
        Entity player = gameWorld.getPlayer();
        PlayerComponent playerComp = player.getComponent(PlayerComponent.class);

        if (playerComp.isPlayersTurn) return;

        // process several actors in a frame
        for (int i = 0; i < ACTOR_PROCESS_COUNT; i++) {
            gameWorld.timeTurnManager.processNext(gameWorld);
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
        dijkstraOverlayRenderer.render(cameraController.getCamera());

        // restore full viewport for HUD rendering
        HdpiUtils.glViewport(0, 0, WIN_W, WIN_H);
        batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0 , WIN_W, WIN_H));

        batch.begin();
        debugConsoleRenderer.render(batch);
        hudRenderer.render(batch);
        batch.end();
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
        dijkstraOverlayRenderer.dispose();
        debugConsoleRenderer.dispose();
        hudRenderer.dispose();
        debugConsole.dispose();
        gameWorld.dispose();
        EventBus.clear();
        Gdx.input.setInputProcessor(null);
    }
}
