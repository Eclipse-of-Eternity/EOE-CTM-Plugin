package ctm.mc.eoe.entities.zombie;

import ctm.mc.eoe.EOEPlugin;
import ctm.mc.eoe.item.player.EntityStatus;
import ctm.mc.eoe.entities.BossManager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;

public class ZombieT1 extends BossManager {

    @Override
    public void tick() {

    }

    @Override
    public EntityStatus status() {
        return new EntityStatus(
                10,
                500,
                1
        );
    }

    @Override
    public void set(LivingEntity entity) {
        entity.setCanPickupItems(false);
        EOEPlugin.get().getNameTagShower().showText(entity, "Test Zombie", status());
    }
    public EntityType getType() {
        return EntityType.ZOMBIE;
    }
}
