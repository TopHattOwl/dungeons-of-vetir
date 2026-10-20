package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.combat.DamageType;
import com.tophattowl.dungeonsofvetir.game.combat.ElementType;
import com.tophattowl.dungeonsofvetir.game.stats.CombatStat;
import com.tophattowl.dungeonsofvetir.game.stats.SaveChannel;
import com.tophattowl.dungeonsofvetir.game.stats.StatCatalog;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * The aggregated result of a {@code StatAggregator} recompute.
 * Read-only from the outside:
 * consumers query scalars, element resistances, save channels and per-part protection rather than the underlying sources
 */
public class DerivedStatsComponent implements Component {

    private final EnumMap<CombatStat, Float> stats = new EnumMap<>(CombatStat.class);
    private final EnumMap<ElementType, Float> resistances = new EnumMap<>(ElementType.class);
    private final EnumMap<SaveChannel, Float> saves = new EnumMap<>(SaveChannel.class);
    private final Map<BodyPart, EnumMap<DamageType, Integer>> protections = new HashMap<>();

    // ---- scalars ----

    public void set(CombatStat stat, float value) {
        stats.put(stat, value);
    }

    public float get(CombatStat stat) {
        return stats.getOrDefault(stat, StatCatalog.get(stat).base());
    }

    public int getInt(CombatStat stat) {
        return Math.round(get(stat));
    }

    // ---- element resistance ----

    public void setResistance(ElementType element, float value) {
        resistances.put(element, value);
    }

    public float resistance(ElementType element) {
        return resistances.getOrDefault(element, 0f);
    }

    // ---- save channels ----

    public void setSave(SaveChannel channel, float value) {
        saves.put(channel, value);
    }

    public float save(SaveChannel channel) {
        return saves.getOrDefault(channel, 0f);
    }

    // ---- per-part protection (flat, per damage type) ----

    public void clearProtections() {
        protections.clear();
    }

    public void setProtection(BodyPart part, DamageType type, int value) {
        protections.computeIfAbsent(part, p -> new EnumMap<>(DamageType.class)).put(type, value);
    }

    public int protection(BodyPart part, DamageType type) {
        EnumMap<DamageType, Integer> byType = protections.get(part);
        return byType == null ? 0 : byType.getOrDefault(type, 0);
    }
}
