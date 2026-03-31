package com.tophattowl.dungeonsofvetir.game.debug;

import java.util.EnumSet;
import java.util.Set;

public class DebugLogger {

    private static final Set<Category> active = EnumSet.of(
        Category.EVENT,
        Category.EQUIP_SYSTEM,
        Category.FACTORY
    );

    public enum Category {
        COMBAT,
        BODY,
        MOVEMENT,
        FOV,
        TURN,
        ACTION,
        EVENT,
        AI,
        INVENTORY,
        FACTORY,
        CONSOLE,
        INPUT,
        FACTION,
        DIJKSTRA,
        SPAWN,
        EQUIPMENT,
        EQUIP_SYSTEM,
    }

    public enum Level {
        INFO,
        WARNING,
        ERROR
    }

    // toggles entire logging on/off
    private static boolean enabled = true;
    private static boolean logAll = false;

    public static void log(Category category, Level level, String location, String message) {
        if(!enabled) return;
        if(!active.contains(category) && !logAll) return;

        String prefix = "\n###################\n# Debug Log\n###################\n"
            + "[" + level + "] " + "[" + category + "] " + "[" + location + "] \n";
        String suffix = "\n###################\n# Log end\n###################\n";
        if (level == Level.ERROR) {
            System.err.println(prefix + message + suffix);
        } else {
            System.out.println(prefix + message + suffix);
        }
    }

    public static void log(Category category, String location, String message) {
        log(category, Level.INFO, location, message);
    }

    // ---------------------
    // Toggle controls
    // ---------------------
    public static void enable(Category category) {active.add(category);}
    public static void disable(Category category) {active.remove(category);}
    public static void toggle(Category category) {
        if (active.contains(category)) active.remove(category);
        else active.add(category);
    }

    public static void setEnabled(boolean b) {
        enabled =  b;
    }

    public static void printStatus() {
        System.out.println("===== DebugLogger Status =====");
        System.out.println("Enabled: " + enabled);
        System.out.println("Active categories: " + active);
        System.out.println("=====        END         =====");
    }
}
