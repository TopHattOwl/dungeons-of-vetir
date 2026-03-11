package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartType;

import java.util.List;
import java.util.Random;

public class BodyComponent implements Component {
    public List<BodyPart> bodyParts;
    private static final Random rng = new Random();

    public BodyComponent(List<BodyPart> bodyParts) {
        this.bodyParts = bodyParts;
    }

    public BodyPart getRandomBodyPart() {
        float roll = rng.nextFloat();
        float cumulative = 0f;
        for (BodyPart part : bodyParts) {
            cumulative += part.hitChance;;
            if (roll < cumulative) return part;
        }

        // fallback
        return bodyParts.get(bodyParts.size()-1);
    }

    /**
     * gets a specific body part by name
     * @param name name of the body part
     * @return the body part with the given name or null
     */
    public BodyPart getPartByName(String name) {
        return bodyParts.stream()
            .filter(p -> p.name.equals(name))
            .findFirst()
            .orElse(null);
    }

    /**
     * @param type the type of the body part
     * @return a body part matching the type (chooses a random if more than one body part of the same type)
     */
    public BodyPart getPartByType(BodyPartType type) {
        BodyPart[] matchingParts = bodyParts.stream()
            .filter(p -> p.type.equals(type))
            .toArray(BodyPart[]::new);
        return matchingParts[rng.nextInt(matchingParts.length)];
    }

    public int getTotalCurrentHp() {
        return bodyParts.stream().mapToInt(p -> p.hp).sum();
    }

    public int getTotalMaxHp() {
        return bodyParts.stream().mapToInt(p -> p.maxHp).sum();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (BodyPart bodyPart : bodyParts) {
            sb.append(bodyPart.toString());
        }
        return sb.toString();
    }
}
