package com.tophattowl.dungeonsofvetir.game.actors.body;

import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType;

import java.util.List;

public class BodyPart {
    private static final float CRIPPLED_THRESHOLD = 0.3f;
    private static final float INJURED_THRESHOLD = 0.6f;

    private static final float BODY_PART_DAMAGE_MULTIPLIER = 0.4f;

    public final String name;
    public final BodyPartType type;
    public final BodyPartRole role;

    // which equipment slots this body part provides
    public final List<EquipmentSlotType> equippableSlots;

    // mutable states
    public int maxHp;
    public int hp;

    public int naturalProtection;

    public float hitWeight; // weight for hit selection
    public float hpShare;
    public float damageMultiplier; // damage multiplier for hp damage

    public BodyPartStatus status;


    public BodyPart(String name, BodyPartType type, BodyPartRole role,
                    List<EquipmentSlotType> equippableSlots, int maxHp, int naturalProtection,
                    float hpShare, float hitWeight, float damageMultiplier) {
        this.name = name;
        this.type = type;
        this.role = role;
        this.equippableSlots = equippableSlots;
        this.maxHp = maxHp;
        this.naturalProtection = naturalProtection;
        this.hp = maxHp;
        this.hpShare = hpShare;
        this.hitWeight = hitWeight;
        this.damageMultiplier = damageMultiplier;
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
        hp = (int) Math.max(0, hp - damage * BODY_PART_DAMAGE_MULTIPLIER);
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
            "\n<<<<<<<<< BODY PART >>>>>>>>>\n"
            + "{BodyPart} " + name + "\n"
            + "type: " + type + ", " + "role: " + role + "\n"
            + "HP: " + hp + "/" + maxHp + "\n"
            + "Natural prots: " + naturalProtection + "\n"
            + "Status: " + status + "\n"
            + "HitChance: " + hitWeight + "\n"
            + "<<<<<<<<< BODY PART >>>>>>>>>\n";
    }
}
