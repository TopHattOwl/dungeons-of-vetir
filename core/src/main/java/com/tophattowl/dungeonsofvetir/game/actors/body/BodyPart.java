package com.tophattowl.dungeonsofvetir.game.actors.body;

import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlot;

import java.util.List;

public class BodyPart {
    private static final float CRIPPLED_THRESHOLD = 0.3f;
    private static final float INJURED_THRESHOLD = 0.6f;

    public final String name;
    public final BodyPartType type;
    public final BodyPartRole role;

    // which equipment slots this body part provides
    public final List<EquipmentSlot> equippableSlots;

    // mutable states
    public int maxHp;
    public int hp;

    public float hitChance; // weight for hit selection, all parts sum to 1.0
    public float hpShare;

    public BodyPartStatus status;


    public BodyPart(String name,BodyPartType type, BodyPartRole role,
                    List<EquipmentSlot> equippableSlots, int maxHp, float hpShare,
                    float hitChance) {
        this.name = name;
        this.type = type;
        this.role = role;
        this.equippableSlots = equippableSlots;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.hpShare = hpShare;
        this.hitChance = hitChance;
        this.status = BodyPartStatus.HEALTHY;
    }

    public boolean isDestroyed() {
        return status == BodyPartStatus.DESTROYED;
    }
    public boolean isVital() {
        return role == BodyPartRole.VITAL;
    }

    /**
     * Body part takes damage, updates status if threshold reached
     * @param damage amount of damage to take
     * @return true if body part was just destroyed
     */
    public boolean applyDamage(int damage) {
        hp = Math.max(0, hp - damage);
        updateStatus();
        return hp == 0;
    }

    private void updateStatus() {
        float ratio = (float) hp/maxHp;

        if (ratio <= 0f)                        status = BodyPartStatus.DESTROYED;
        else if (ratio <= CRIPPLED_THRESHOLD)   status = BodyPartStatus.CRIPPLED;
        else if (ratio <= INJURED_THRESHOLD)    status = BodyPartStatus.INJURED;
        else                                    status = BodyPartStatus.HEALTHY;
    }

    @Override
    public String toString() {
        return
            "<<<<<<<<< BODY PART >>>>>>>>>\n"
            + "{BodyPart} " + name + "\n"
            + "type: " + type + ", " + "role: " + role + "\n"
            + "HP: " + hp + "/" + maxHp + "\n"
            + "Status: " + status + "\n"
            + "<<<<<<<<< BODY PART >>>>>>>>>\n";
    }
}
