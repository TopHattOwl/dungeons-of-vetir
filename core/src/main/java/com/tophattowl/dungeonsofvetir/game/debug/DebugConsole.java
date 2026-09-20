package com.tophattowl.dungeonsofvetir.game.debug;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.ActorId;
import com.tophattowl.dungeonsofvetir.game.actors.faction.FactionRelation;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.input.ConsoleActiveChangedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.input.ConsoleToggleRequestedEvent;
import com.tophattowl.dungeonsofvetir.game.factory.actors.EntityFactory;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Point;
import com.tophattowl.dungeonsofvetir.util.dijkstra.DijkstraMapType;

import java.util.ArrayList;
import java.util.List;

/**
 * Command model for the debug console
 * <p>
 * Display-free: the Scene2D view and the input handler observe it via events
 */
public class DebugConsole {

    private static final int MAX_OUTPUT_LINES = 300;

    private boolean active = false;
    private final List<String> outputLines = new ArrayList<>();
    private final List<String> history = new ArrayList<>();
    private int historyIndex = 0;
    private String historyDraft = "";

    private GameWorld gameWorld;
    private DijkstraOverlayControl dijkstraControl;

    private final List<EventBus.ListenerHandle<?>> listenerHandles = new ArrayList<>();

    public DebugConsole() {
        listenerHandles.add(EventBus.on(ConsoleToggleRequestedEvent.class, e -> toggle()));
        addOutput("Debug Console ready. Type 'help' for commands.");
    }

    public void setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public void setDijkstraOverlayControl(DijkstraOverlayControl dijkstraControl) {
        this.dijkstraControl = dijkstraControl;
    }

    // --------------------------------------------------
    // visibility
    public boolean isActive() { return active; }

    public void toggle() { setActive(!active); }

    public void setActive(boolean active) {
        if (this.active == active) return;
        this.active = active;
        EventBus.emit(new ConsoleActiveChangedEvent(active));
    }

    // --------------------------------------------------
    // input / history
    public void submit(String rawCommand) {
        String command = rawCommand == null ? "" : rawCommand.trim();
        if (command.isEmpty()) return;

        addHistory(command);
        addOutput("> " + command);

        String result;
        try {
            result = executeCommand(command);
        } catch (Exception e) {
            String message = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            result = "Error: " + message;
        }

        if (result != null && !result.isEmpty()) {
            addOutput(result);
        }
    }

    private void addHistory(String command) {
        if (history.isEmpty() || !history.get(history.size() - 1).equals(command)) {
            history.add(command);
        }
        historyIndex = history.size();
        historyDraft = "";
    }

    /**
     * Cycles command history. {@code currentText} is preserved as a draft while the
     * user browses, and restored when navigating past the newest entry.
     *
     * @return the command line to display, or null when there is no history
     */
    public String navigateHistory(int direction, String currentText) {
        if (history.isEmpty()) return null;

        if (direction < 0) {
            if (historyIndex == history.size()) {
                historyDraft = currentText;
            }
            if (historyIndex > 0) historyIndex--;
            return history.get(historyIndex);
        }

        if (historyIndex < history.size() - 1) {
            historyIndex++;
            return history.get(historyIndex);
        }
        historyIndex = history.size();
        return historyDraft;
    }

    // --------------------------------------------------
    // command execution
    private String executeCommand(String command) {
        String[] parts = command.split("\\s+", 2);
        String cmd = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1] : "";

        return switch (cmd) {
            case "help" -> helpCommand();
            case "clear" -> clearCommand();
            case "spawn" -> spawnCommand(args);
            case "warp" -> warpCommand(args);
            case "log" -> logCommand(args);
            case "factionrel" -> logFactionRelations();
            case "dijkstra" -> dijkstraCommand(args);
            case "entity_list" -> listEntitiesCommand();
            case "entity_info" -> entityInfoCommand(args);
            case "exit", "quit" -> {
                setActive(false);
                yield "Console closed.";
            }
            default -> "Unknown command: " + cmd + ". Type 'help' for available commands.";
        };
    }

    private String helpCommand() {
        return """
            Available commands:
              help           - Show this help message
              clear          - Clear the console output
              spawn <actor_id> - Spawn an actor near the player
              warp <floor>   - Jump to a generated floor
              log <Category> [on/off] - Toggle debug logging category
              factionRel     - Logs faction relations in debug logger
              dijkstra [TYPE] - Toggle dijkstra map overlay (PLAYER, FACTION_MONSTER, etc.)
              entity_list - lists all entities
              entity_info [id] - gives info about entity with given id
              exit, quit     - Close the console
            """;
    }

    private String clearCommand() {
        outputLines.clear();
        return null;
    }

    private String spawnCommand(String args) {
        if (gameWorld == null) {
            return "Error: GameWorld not initialized.";
        }

        if (args.isEmpty()) {
            return "Usage: spawn <actor>. Available: IRON_WORM, SCAVENGER";
        }

        String actorName = args.toUpperCase().trim();
        ActorId actorId;

        try {
            actorId = ActorId.valueOf(actorName);
        } catch (IllegalArgumentException e) {
            return "Unknown actor: " + actorName + ". Available: IRON_WORM, SCAVENGER";
        }

        if (actorId == ActorId.PLAYER) {
            return "Cannot spawn player.";
        }

        var playerPos = gameWorld.getPlayer().getComponent(com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent.class);
        int spawnX = playerPos.getX() + 1;
        int spawnY = playerPos.getY();
        var pos = new Point(spawnX, spawnY);

        EntityFactory.createEntity(actorId, gameWorld, pos);
        return "Spawned " + actorName + " at (" + spawnX + ", " + spawnY + ")";
    }

    private String warpCommand(String args) {
        if (gameWorld == null) {
            return "Error: GameWorld not initialized.";
        }

        if (args.isEmpty()) {
            return "Usage: warp <floor>";
        }

        int floor;
        try {
            floor = Integer.parseInt(args.trim());
        } catch (NumberFormatException e) {
            return "Usage: warp <floor>";
        }

        if (floor < 1) {
            return "Floor must be >= 1.";
        }

        gameWorld.enterLevel(floor);
        var resolved = gameWorld.getCurrentResolved();
        return "Warped to floor " + floor
            + " | " + resolved.section().name()
            + " - " + resolved.variation().displayName()
            + " (" + resolved.role() + ")";
    }

    private String logCommand(String args) {
        if (args.isEmpty()) {
            return "Usage: log <category> [on/off]. Categories: COMBAT, BODY, MOVEMENT, FOV, TURN, ACTION, EVENT, AI, INVENTORY, FACTORY";
        }

        String[] parts = args.split("\\s+");
        String categoryName = parts[0].toUpperCase();
        boolean enable = parts.length > 1 && parts[1].equalsIgnoreCase("on");

        try {
            DebugLogger.Category category = DebugLogger.Category.valueOf(categoryName);
            if (enable) {
                DebugLogger.enable(category);
            } else if (parts.length > 1 && parts[1].equalsIgnoreCase("off")) {
                DebugLogger.disable(category);
            } else {
                boolean isEnabled = DebugLogger.Category.valueOf(categoryName) != null;
                return "Category " + categoryName + " is " + (isEnabled ? "enabled" : "disabled");
            }
            return "Category " + categoryName + " " + (enable ? "enabled" : "disabled");
        } catch (IllegalArgumentException e) {
            return "Unknown category: " + categoryName;
        }
    }

    private String logFactionRelations() {
        FactionRelation.logFactionRelations();
        return "Faction relations logged in debug logger";
    }

    private String dijkstraCommand(String args) {
        if (dijkstraControl == null) {
            return "Error: Dijkstra overlay not initialized.";
        }

        if (!args.isEmpty()) {
            String typeName = args.toUpperCase().trim();
            try {
                DijkstraMapType mapType = DijkstraMapType.valueOf(typeName);
                dijkstraControl.setMapType(mapType);
            } catch (IllegalArgumentException e) {
                return "Unknown dijkstra map type: " + typeName + ". Available: PLAYER, FACTION_MONSTER, FACTION_HUNTER, FACTION_LOOTER";
            }
        }

        dijkstraControl.toggle();
        String state = dijkstraControl.isEnabled() ? "enabled" : "disabled";
        String type = dijkstraControl.getMapType().name();
        return "Dijkstra overlay " + state + " (type: " + type + ")";
    }

    private void addOutput(String text) {
        if (text == null) return;

        // each buffer entry is a single visual line, so line caps and history are accurate
        for (String line : text.split("\r?\n", -1)) {
            outputLines.add(line);
        }

        while (outputLines.size() > MAX_OUTPUT_LINES) {
            outputLines.remove(0);
        }
    }

    private String listEntitiesCommand() {
        StringBuilder sb = new StringBuilder();
        List<Entity> entities = gameWorld.getAllEntities();
        for (Entity entity : entities) {
            sb.append(entity).append("\n");
        }

        return sb.toString();
    }

    private String entityInfoCommand(String args) {
        int id = Integer.parseInt(args.trim());

        return gameWorld.getEntity(id).getAllInfo();
    }

    public List<String> getOutputLines() {
        return outputLines;
    }

    public void dispose() {
        listenerHandles.forEach(EventBus::off);
        listenerHandles.clear();
    }
}
