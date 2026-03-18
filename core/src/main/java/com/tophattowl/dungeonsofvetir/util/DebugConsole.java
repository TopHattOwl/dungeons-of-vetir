package com.tophattowl.dungeonsofvetir.util;

import com.tophattowl.dungeonsofvetir.game.actors.ActorId;
import com.tophattowl.dungeonsofvetir.game.actors.faction.FactionRelation;
import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.input.ConsoleRequestedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.input.UiKeyTypedEvent;
import com.tophattowl.dungeonsofvetir.game.factory.actors.EntityFactory;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Point;

import java.util.ArrayList;
import java.util.List;

public class DebugConsole {
    private boolean active = false;
    private final StringBuilder inputBuffer = new StringBuilder();
    private final List<String> outputLines = new ArrayList<>();

    private static final int MAX_OUTPUT_LINES = 50;

    private GameWorld gameWorld;

    private final List<EventBus.ListenerHandle<?>> listenerHandles = new ArrayList<>();

    public DebugConsole() {
        listenerHandles.add(EventBus.on(ConsoleRequestedEvent.class, e -> {
            toggle();
        }));
        listenerHandles.add(EventBus.on(UiKeyTypedEvent.class, e -> {
            handleChar(e.keyChar());
        }));

        addOutput("Debug Console ready. Type 'help' for commands.");
    }

    public void setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    // --------------------------------------------------
    // visibility
    public void toggle() { active = !active; }
    public boolean isActive() { return active; }

    // --------------------------------------------------
    // input -- called from InputHandler
    public void handleChar(char c) {
        if (!active) return;

        // backspace
        if (c == '\b') {
            if (!inputBuffer.isEmpty()) {
                inputBuffer.deleteCharAt(inputBuffer.length() - 1);
            }
            return;
        }

        if (c == '\r' || c == '\n') {
            submit();
        } else  {
            inputBuffer.append(c);
        }
    }



    // --------------------------------------------------
    // command execution
    private void submit() {
        String command = inputBuffer.toString().trim();
        inputBuffer.setLength(0);

        if (command.isEmpty()) {
            return;
        }

        addOutput("> " + command);

        String result = executeCommand(command);
        if (result != null && !result.isEmpty()) {
            addOutput(result);
        }
    }

    private String executeCommand(String command) {
        String[] parts = command.split("\\s+", 2);
        String cmd = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1] : "";

        return switch (cmd) {
            case "help" -> helpCommand();
            case "clear" -> clearCommand();
            case "spawn" -> spawnCommand(args);
            case "log" -> logCommand(args);
            case "factionrel" -> logFactionRelations();
            case "exit", "quit" -> {
                toggle();
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
              log <Category> [on/off] - Toggle debug logging category
              factionRel     - Logs faction relations in debug logger
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
            return "Usage: spawn <actor>. Available: IRON_WORM, CAVE_BAT, SCAVENGER";
        }

        String actorName = args.toUpperCase().trim();
        ActorId actorId;

        try {
            actorId = ActorId.valueOf(actorName);
        } catch (IllegalArgumentException e) {
            return "Unknown actor: " + actorName + ". Available: IRON_WORM, CAVE_BAT, SCAVENGER";
        }

        if (actorId == ActorId.PLAYER) {
            return "Cannot spawn player.";
        }

        var playerPos = gameWorld.getPlayer().getComponent(com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent.class);
        int spawnX = playerPos.getX() + 1;
        int spawnY = playerPos.getY();
        var pos = new Point(spawnX, spawnY);

        var entity = EntityFactory.createEntity(actorId, gameWorld, pos);
        return "Spawned " + actorName + " at (" + spawnX + ", " + spawnY + ")";
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

    private void addOutput(String line) {
        outputLines.add(line);
        while (outputLines.size() > MAX_OUTPUT_LINES) {
            outputLines.remove(0);
        }
    }

    public List<String> getOutputLines() {
        return outputLines;
    }

    public String getInputString() {
        return inputBuffer.toString();
    }

    public void dispose() {
        listenerHandles.forEach(EventBus::off);
        listenerHandles.clear();
    }
}
