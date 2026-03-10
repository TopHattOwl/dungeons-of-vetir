package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;

import java.util.List;

public class BodyComponent implements Component {
    public List<BodyPart> bodyParts;

    public BodyComponent(List<BodyPart> bodyParts) {
        this.bodyParts = bodyParts;
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
