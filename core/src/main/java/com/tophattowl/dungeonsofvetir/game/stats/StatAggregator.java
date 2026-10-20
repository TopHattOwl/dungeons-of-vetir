package com.tophattowl.dungeonsofvetir.game.stats;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;
import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.components.AttributesComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.BodyComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.DerivedStatsComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.EquipmentComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.HealthComponent;
import com.tophattowl.dungeonsofvetir.game.combat.DamageType;
import com.tophattowl.dungeonsofvetir.game.combat.ElementType;
import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.game.items.components.ItemComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

/**
 * Derives an entity's stats from its attributes and every {@link ModifierSource} it carries
 * (its own components plus equipped items) <br>
 * Recompute is idempotent and triggered on spawn, on {@code EquipmentChangedEvent} and (later) on status/expertise changes
 * <p>
 * All modifiers are gathered then applied globally
 */
public class StatAggregator {

    /** Save channels are a fraction of the governing attribute per point. */
    private static final float SAVE_PER_ATTRIBUTE = 0.01f;
    private static final float SAVE_CAP = 0.9f;

    public void recompute(Entity entity) {
        AttributesComponent attributes = entity.getComponent(AttributesComponent.class);
        if (attributes == null) {
            return; // statless entity (the turn clock)
        }

        List<Modifier> modifiers = gatherModifiers(entity);

        for (Attribute attribute : Attribute.values()) {
            float value = apply(modifiers, attribute, attributes.base(attribute));
            attributes.setEffective(attribute, value);
        }

        DerivedStatsComponent derived = entity.getComponent(DerivedStatsComponent.class);
        if (derived == null) {
            derived = new DerivedStatsComponent();
            entity.addComponent(derived);
        }

        for (StatGrowth growth : StatCatalog.all()) {
            float value = baseValue(growth, attributes);
            value = apply(modifiers, growth.stat(), value);
            if (growth.hasCap()) {
                value = Math.min(value, growth.cap());
            }
            derived.set(growth.stat(), value);
        }

        applySaves(attributes, derived);
        applyResistances(derived);
        rebuildProtections(entity, derived);
        applyHealth(entity, derived);
    }

    private float baseValue(StatGrowth growth, AttributesComponent attributes) {
        float value = growth.base();
        if (!growth.isModifierOnly()) {
            float attribute = attributes.effective(growth.owner());
            value += growth.perPoint() * attribute;
            value += growth.perFive() * (float) Math.floor(attribute / 5f);
        }
        return value;
    }

    private float apply(List<Modifier> modifiers, StatRef target, float base) {
        float flat = 0f;
        float percent = 0f;
        float multiply = 1f;
        for (Modifier modifier : modifiers) {
            if (!modifier.target().equals(target)) continue;
            switch (modifier.operation()) {
                case FLAT -> flat += modifier.value();
                case PERCENT -> percent += modifier.value();
                case MULTIPLY -> multiply *= modifier.value();
            }
        }
        return (base + flat) * (1f + percent) * multiply;
    }

    private List<Modifier> gatherModifiers(Entity entity) {
        List<Modifier> out = new ArrayList<>();

        for (Component component : entity.getComponents()) {
            if (component instanceof ModifierSource source) {
                source.collectModifiers(out);
            }
        }

        EquipmentComponent equipment = entity.getComponent(EquipmentComponent.class);
        if (equipment != null) {
            Set<Item> seen = Collections.newSetFromMap(new IdentityHashMap<>());
            for (EquipmentComponent.EquipmentSlot slot : equipment.equipment) {
                if (slot.item == null || !seen.add(slot.item)) continue;
                for (ItemComponent component : slot.item.getComponents()) {
                    if (component instanceof ModifierSource source) {
                        source.collectModifiers(out);
                    }
                }
            }
        }

        return out;
    }

    private void applySaves(AttributesComponent attributes, DerivedStatsComponent derived) {
        for (SaveChannel channel : SaveChannel.values()) {
            float value = switch (channel) {
                case BLEED, POISON, DISEASE ->
                    attributes.effective(Attribute.FLESH) * SAVE_PER_ATTRIBUTE;
                case STUN, KNOCKBACK ->
                    attributes.effective(Attribute.THEW) * SAVE_PER_ATTRIBUTE;
                case CORRUPTION, PAIN, MENTAL ->
                    attributes.effective(Attribute.GRIT) * SAVE_PER_ATTRIBUTE;
            };
            derived.setSave(channel, Math.min(value, SAVE_CAP));
        }
    }

    private void applyResistances(DerivedStatsComponent derived) {
        derived.setResistance(ElementType.PHYSICAL, derived.get(CombatStat.RESIST_PHYSICAL));
        derived.setResistance(ElementType.FIRE, derived.get(CombatStat.RESIST_FIRE));
        derived.setResistance(ElementType.LIGHTNING, derived.get(CombatStat.RESIST_LIGHTNING));
        derived.setResistance(ElementType.POISON, derived.get(CombatStat.RESIST_POISON));
        derived.setResistance(ElementType.ICE, derived.get(CombatStat.RESIST_ICE));
    }

    /**
     * Protection is flat per body part per damage type. Armor contributions land here in
     * phase 3C; for now each part contributes its natural protection (single value applied to
     * every damage type).
     */
    private void rebuildProtections(Entity entity, DerivedStatsComponent derived) {
        derived.clearProtections();
        BodyComponent body = entity.getComponent(BodyComponent.class);
        if (body == null) return;

        for (BodyPart part : body.bodyParts) {
            for (DamageType type : DamageType.values()) {
                derived.setProtection(part, type, part.naturalProtection);
            }
        }
    }

    private void applyHealth(Entity entity, DerivedStatsComponent derived) {
        int maxHp = Math.max(1, derived.getInt(CombatStat.MAX_HP));

        HealthComponent health = entity.getComponent(HealthComponent.class);
        if (health == null) {
            entity.addComponent(new HealthComponent(maxHp));
        } else {
            health.maxHp = maxHp;
            if (health.hp > maxHp) health.hp = maxHp;
        }

        BodyComponent body = entity.getComponent(BodyComponent.class);
        if (body == null) return;

        for (BodyPart part : body.bodyParts) {
            int partMax = Math.max(1, Math.round(maxHp * part.hpShare));
            // Body parts are percentage-based: preserve the current condition ratio when the
            // max changes (a full part stays full; a damaged part keeps its percentage).
            float ratio = part.maxHp > 0 ? (float) part.hp / part.maxHp : 1f;
            part.maxHp = partMax;
            part.hp = Math.min(partMax, Math.round(partMax * ratio));
        }
    }
}
