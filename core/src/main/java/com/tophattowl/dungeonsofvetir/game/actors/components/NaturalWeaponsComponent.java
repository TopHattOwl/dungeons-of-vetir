package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.monsters.NaturalWeapon;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaturalWeaponsComponent implements Component {

    private final Map<BodyPart, NaturalWeapon> naturalWeapons;
    private final List<BodyPart> sortedParts;

    public NaturalWeaponsComponent(Map<BodyPart, NaturalWeapon> naturalWeapons) {
        this.naturalWeapons = new HashMap<>(naturalWeapons);

        this.sortedParts = naturalWeapons.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(
                Comparator.comparingDouble(NaturalWeapon::getAttackChance)
            ))
            .map(Map.Entry::getKey)
            .toList();
    }

    public List<BodyPart> getSortedParts() {
        return sortedParts;
    }

    public List<NaturalWeapon> getNaturalWeaponsSorted() {
        return sortedParts.stream()
            .map(this::getNaturalWeapon)
            .toList();
    }

    public NaturalWeapon getNaturalWeapon(BodyPart part) {
        return naturalWeapons.get(part);
    }

    @Override
    public String toString() {
        return "NaturalWeaponsComponent{" +
            "naturalWeapons=" + naturalWeapons +
            '}';
    }
}
