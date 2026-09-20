package com.tophattowl.dungeonsofvetir.display.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tophattowl.dungeonsofvetir.display.assets.TextureRegistry;
import com.tophattowl.dungeonsofvetir.display.camera.CameraController;
import com.tophattowl.dungeonsofvetir.display.renderer.DijkstraOverlayRenderer;
import com.tophattowl.dungeonsofvetir.display.renderer.FovOverlayRenderer;
import com.tophattowl.dungeonsofvetir.display.renderer.WorldRenderer;
import com.tophattowl.dungeonsofvetir.display.sprites.SpriteLibrary;
import com.tophattowl.dungeonsofvetir.display.tilesets.TerrainTilesetRegistry;
import com.tophattowl.dungeonsofvetir.display.ui.debug.DebugConsoleSkinFactory;
import com.tophattowl.dungeonsofvetir.display.ui.debug.DebugConsoleView;
import com.tophattowl.dungeonsofvetir.display.ui.HudRenderer;
import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.action.ActionHandler;
import com.tophattowl.dungeonsofvetir.game.action.EquipAction;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.components.EquipmentComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.PlayerComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.event.events.LevelChangedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.input.ConsoleActiveChangedEvent;
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
    // Layout constants (virtual design resolution; scaled to the window at runtime)
    private static final int VIRTUAL_W  = 1280;
    private static final int VIRTUAL_H  = 800;
    private static final int TOP_BAR_H  = 32;
    private static final int SIDE_W     = HudRenderer.SIDE_W;
    private static final int BOTTOM_H   = 96;
    public  static final int VIEWPORT_W = VIRTUAL_W - SIDE_W;             // 1024
    public  static final int VIEWPORT_H = VIRTUAL_H - TOP_BAR_H - BOTTOM_H; // 672
    public  static final int VIEWPORT_X = 0;
    public  static final int VIEWPORT_Y = BOTTOM_H;                       // 96

    public  static final int ACTOR_PROCESS_COUNT = 10;

    // display
    private SpriteBatch batch;
    private BitmapFont font;
    private TextureRegistry textures;
    private SpriteLibrary sprites;
    private TerrainTilesetRegistry terrains;
    private WorldRenderer worldRenderer;
    private FovOverlayRenderer fovOverlayRenderer;
    private DijkstraOverlayRenderer dijkstraOverlayRenderer;
    private CameraController cameraController;
    private HudRenderer hudRenderer;
    private Viewport viewport;
    private Stage stage;
    private Skin uiSkin;
    private DebugConsoleView debugConsoleView;

    // game
    private GameWorld gameWorld;
    private InputHandler inputHandler;
    private DebugConsole debugConsole;

    @Override
    public void show() {
        // game first: the tileset palette depends on the starting floor
        gameWorld = new GameWorld(SeedConfig.custom(178439));
        inputHandler = new InputHandler(gameWorld.getPlayer());
        debugConsole = new DebugConsole();

        // display
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        textures = new TextureRegistry();
        sprites = new SpriteLibrary(textures);
        terrains = new TerrainTilesetRegistry(textures);

        viewport = new FitViewport(VIRTUAL_W, VIRTUAL_H);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        cameraController = new CameraController(VIEWPORT_W, VIEWPORT_H);
        worldRenderer = new WorldRenderer(
            batch, terrains.get(gameWorld.getCurrentResolved().theme()), sprites
        );
        fovOverlayRenderer = new FovOverlayRenderer();
        worldRenderer.setFovOverlayRenderer(fovOverlayRenderer);
        dijkstraOverlayRenderer = new DijkstraOverlayRenderer(batch, font);
        hudRenderer = new HudRenderer(VIRTUAL_W, VIRTUAL_H, font);

        // Scene2D debug console
        uiSkin = DebugConsoleSkinFactory.create(font);
        debugConsoleView = new DebugConsoleView(debugConsole, uiSkin);
        debugConsoleView.setPosition(
            (VIRTUAL_W - debugConsoleView.getWidth()) / 2f,
            (VIRTUAL_H - debugConsoleView.getHeight()) / 2f
        );
        stage = new Stage(viewport);
        stage.addActor(debugConsoleView);
        setConsoleVisible(false);

        debugConsole.setGameWorld(gameWorld);
        dijkstraOverlayRenderer.setGameWorld(gameWorld);
        debugConsole.setDijkstraOverlayControl(dijkstraOverlayRenderer);

        hudRenderer.setPlayer(gameWorld.getPlayer());

        gameWorld.updateFov();
        gameWorld.addDijkstraMapManager(new DijkstraMapManager(gameWorld));

        Gdx.input.setInputProcessor(new InputMultiplexer(inputHandler, stage));

        EventBus.on(LevelChangedEvent.class, this::onLevelChanged);
        EventBus.on(ConsoleActiveChangedEvent.class, this::onConsoleActiveChanged);

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

    private void onLevelChanged(LevelChangedEvent event) {
        worldRenderer.setTerrain(terrains.get(event.resolved().theme()));
        PositionComponent pos = gameWorld.getPlayer().getComponent(PositionComponent.class);
        cameraController.centerOn(pos.getX(), pos.getY());
    }

    private void onConsoleActiveChanged(ConsoleActiveChangedEvent event) {
        setConsoleVisible(event.active());
    }

    private void setConsoleVisible(boolean visible) {
        debugConsoleView.setVisible(visible);
        debugConsoleView.setTouchable(visible ? Touchable.enabled : Touchable.disabled);

        if (visible) {
            debugConsoleView.refresh();
            stage.setKeyboardFocus(debugConsoleView.getInputField());
            debugConsoleView.focusInput();
        } else {
            stage.setKeyboardFocus(null);
        }
    }

    @Override
    public void render(float v) {
        input();
        logic();
        stage.act(v);
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
            gameWorld.updateFov();
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

        // World renders into the game sub-rectangle of the scaled virtual screen.
        // The virtual layout is scaled uniformly by the FitViewport, so the sub-rect
        // is the virtual region transformed by the viewport's scale + letterbox offset.
        float scale = viewport.getScreenWidth() / (float) VIRTUAL_W;
        int worldX = viewport.getScreenX() + Math.round(VIEWPORT_X * scale);
        int worldY = viewport.getScreenY() + Math.round(VIEWPORT_Y * scale);
        int worldW = Math.round(VIEWPORT_W * scale);
        int worldH = Math.round(VIEWPORT_H * scale);

        HdpiUtils.glViewport(worldX, worldY, worldW, worldH);

        batch.setProjectionMatrix(cameraController.getCamera().combined);
        batch.begin();
        worldRenderer.render(gameWorld, cameraController.getCamera());
        batch.end();

        fovOverlayRenderer.render(gameWorld, cameraController.getCamera());
        dijkstraOverlayRenderer.render(cameraController.getCamera());

        // Full virtual screen for the HUD, in virtual pixel coordinates.
        viewport.apply();
        Matrix4 hudProjection = viewport.getCamera().combined;
        batch.setProjectionMatrix(hudProjection);
        hudRenderer.setProjectionMatrix(hudProjection);

        batch.begin();
        hudRenderer.render(batch);
        batch.end();

        // console overlay on top, in virtual pixel coordinates
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
        terrains.dispose();
        textures.dispose();
        fovOverlayRenderer.dispose();
        dijkstraOverlayRenderer.dispose();
        hudRenderer.dispose();
        debugConsoleView.dispose();
        inputHandler.dispose();
        stage.dispose();
        uiSkin.dispose();
        debugConsole.dispose();
        gameWorld.dispose();
        EventBus.clear();
        Gdx.input.setInputProcessor(null);
    }
}
