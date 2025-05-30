package ctm.mc.eoe.entities;


import ctm.mc.eoe.entities.zombie.ZombieT1;
import ctm.mc.eoe.item.player.EntityStatus;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;

public abstract class BossManager {
    static HashMap<String, BossManager> stringSlayerBossHashMap = new HashMap<>();
    public static void spawn(Location location, String id){
        BossManager slayerBoss = stringSlayerBossHashMap.get(id);
        EntityManager.spawn(
                location,
                slayerBoss.getType().getEntityClass(),
                slayerBoss.status(),
                (entity)-> {
                    slayerBoss.set((LivingEntity) entity);
                    entity.addScoreboardTag("boss_eoe");
                    NBT.modify(entity, nbt -> {
                        nbt.setString("BOSS_ID", id);
                        nbt.setDouble("health", ((LivingEntity) entity).getHealth());
                    });
//                    NBTTagCompound nbtTagCompound = NBTUtils.getNBTFromEntity(entity);
//                    nbtTagCompound.a("health", ((LivingEntity) entity).getHealth());
//                    nbtTagCompound.a("BOSS_ID", id);
//                    NBTUtils.setNBTFromEntity(entity, nbtTagCompound);

                    ((LivingEntity) entity).setMaxHealth(1);
                    ((LivingEntity) entity).setHealth(1);
                }
        );
    }
    public static void load(){
        stringSlayerBossHashMap.put("test", new ZombieT1());
    }
    public static boolean hasBoss(String a){
        return stringSlayerBossHashMap.containsKey(a);
    }
    public static void addOfflineBoss(Entity entity){
//        NBTTagCompound nbtTagCompound = NBTUtils.getNBTFromEntity(entity);

        ReadWriteNBT entityNbt = NBT.createNBTObject();
        NBT.get(entity, entityNbt::mergeCompound);

        String id = entityNbt.getString("BOSS_ID");
        double health = entityNbt.getDouble("health");

        EntityManager.entityStatusHashMap.put(
                entity.getUniqueId(),
                stringSlayerBossHashMap.get(id).status()
        );
        EntityManager.get(entity).health = (float) health;
    }
    public static boolean isOfflineBoss(Entity entity){
        return entity.getScoreboardTags().contains("boss_eoe");
    }
    public static void update(Entity entity){
//        NBTTagCompound nbtTagCompound = NBTUtils.getNBTFromEntity(entity);
//        nbtTagCompound.a("health", ((LivingEntity) entity).getHealth());
//        NBTUtils.setNBTFromEntity(entity, nbtTagCompound);
        NBT.modify(entity, nbt -> {
            nbt.setDouble("health", ((LivingEntity) entity).getHealth());
        });
    }
    public abstract void tick();
    public abstract EntityStatus status();
    public abstract void set(LivingEntity entity);
    public abstract EntityType getType();
}
