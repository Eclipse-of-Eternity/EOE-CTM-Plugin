package ctm.mc.eoe.entities;

import ctm.mc.eoe.item.player.EntityStatus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class EntityManager {
    public static HashMap<UUID, EntityStatus> entityStatusHashMap = new HashMap<>();
    public static void spawn(Location location, Class<? extends Entity> entityType, EntityStatus entityStatus, Consumer<? super Entity> consumer){
        Entity entity;
        entityStatusHashMap.put((entity = location.getWorld().spawn(
                location,
                entityType
        )).getUniqueId(), entityStatus);
        consumer.accept(entity);
    }
    public static EntityStatus get(Entity entity){
        if(entity instanceof LivingEntity livingEntity){
            return entityStatusHashMap.get(livingEntity.getUniqueId());
        }
        return null;
    }
    public static void tick(){
        for(UUID entity1 : entityStatusHashMap.keySet().toArray(new UUID[0])){
            if(Bukkit.getEntity(entity1) != null && Bukkit.getEntity(entity1).isDead()){
                entityStatusHashMap.remove(entity1);
            }
        }
    }
}
