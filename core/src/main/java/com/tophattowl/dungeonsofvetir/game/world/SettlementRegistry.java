package com.tophattowl.dungeonsofvetir.game.world;

import java.util.HashMap;
import java.util.Map;

/**
 * Content table of settlements. Empty for now; settlement content lands with the overworld
 */
public final class SettlementRegistry {

    private static final Map<String, SettlementDefinition> settlements = new HashMap<>();

    private SettlementRegistry() {}

    public static void register(SettlementDefinition definition) {
        settlements.put(definition.key(), definition);
    }

    public static SettlementDefinition get(String key) {
        return settlements.get(key);
    }
}
