package com.tophattowl.dungeonsofvetir.game.stats;

/**
 * The five innate attributes. Attributes are the inputs to the stat system: <br>
 * they are raised passively (expertise, gear) and each point raises the stats they govern
 * <p>
 * Attributes are also read directly by checks (forcing a door tests Thew, swimming tests Wind), Checks are not stats
 */
public enum Attribute implements StatRef {

    THEW("Thew", "The raw strength of your body"),
    WIND("Wind", "Your mobility and stamina"),
    FLESH("Flesh", "The durability of your flesh"),
    BLOOD("Blood", "The Ancients' blood flows through your veins"),
    GRIT("Grit", "The endurance of your mind"),
    ;

    public final String displayName;
    public final String description;

    Attribute(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
}
