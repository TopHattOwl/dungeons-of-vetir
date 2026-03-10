package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;

public class RenderableComponent implements Component {
    public final String spriteId;
    public final int renderOrder;
    public boolean visible;

    public RenderableComponent(String spriteId, int renderOrder) {
        this.spriteId = spriteId;
        this.renderOrder = renderOrder;
        this.visible = true;
    }

    public RenderableComponent(String spriteId) {
        this(spriteId, 1);
    }
}
