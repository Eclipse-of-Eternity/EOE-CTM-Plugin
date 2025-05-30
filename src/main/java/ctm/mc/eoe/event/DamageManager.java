package ctm.mc.eoe.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;


public class DamageManager {
    public static void damage(Entity entity, Player player, double dmg){
        if(!(entity instanceof LivingEntity livingEntity)) return;
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(
                player,
                entity,
                EntityDamageEvent.DamageCause.SONIC_BOOM,
                dmg
        );
        livingEntity.damage(dmg, player);
        entity.setLastDamageCause(event);
        Bukkit.getPluginManager().callEvent(
                event
        );
    }
}
