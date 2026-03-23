package com.tophattowl.dungeonsofvetir.game.factory.items.templates;

import com.tophattowl.dungeonsofvetir.game.items.ItemId;
import com.tophattowl.dungeonsofvetir.game.items.ItemType;

public class MeleeWeaponTemplate {
    public final ItemType itemType = ItemType.MELEE_WEAPON;
    public final ItemId itemID;

    public final String itemName;

    public MeleeWeaponTemplate(Builder b) {
        this.itemID = b.itemID;
        this.itemName = b.itemName;
    }


    public static class Builder {
        // required
        public final ItemType itemType;
        public final ItemId itemID;
        public final String itemName;

        // defaults

        public Builder(ItemType itemType, ItemId itemID, String itemName) {
            this.itemType = itemType;
            this.itemID = itemID;
            this.itemName = itemName;
        }

        public MeleeWeaponTemplate build() {
            return new MeleeWeaponTemplate(this);
        }
    }
}
