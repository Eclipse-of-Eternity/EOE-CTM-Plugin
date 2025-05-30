package ctm.mc.eoe.item.player;

import lombok.*;
public class EntityStatus {
    public float damage;
    public float health;
    public float mhealth;
    public int bossType;
    public EntityStatus(float damage, float health, int bossID){
        this.damage = damage;
        this.health = health;
        this.mhealth = health;
        this.bossType = bossID;
    }
}
