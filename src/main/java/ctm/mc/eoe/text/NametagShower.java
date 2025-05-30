package ctm.mc.eoe.text;

import lombok.Getter;
import ctm.mc.eoe.entities.EntityManager;
import ctm.mc.eoe.item.player.EntityStatus;
import org.bukkit.entity.*;

import java.util.HashMap;
import java.util.UUID;


public class NametagShower {
    public static class Store {
        LivingEntity entity;
        EntityStatus s;
        String name;
        public Store(LivingEntity entity, String name, EntityStatus armorStand){
            this.entity = entity;
            this.name = name;
            this.s = armorStand;
        }
    }
    @Getter
    HashMap<UUID, Store> stores = new HashMap<>();
    public void showText(Entity entity2, String name, EntityStatus status) {
        if(!(entity2 instanceof LivingEntity livingEntity)) return;
        if((livingEntity instanceof ArmorStand)) return;
        if((livingEntity instanceof Player)) return;
        if(stores.containsKey(entity2.getUniqueId())) return;
        livingEntity.setCustomNameVisible(false);
        stores.put(entity2.getUniqueId(), new Store(livingEntity, name, status));
    }
    public void tick() {
        for (Store e2 : stores.values().toArray(new Store[0])) {
            if(e2.entity.isDead()){
                stores.remove(e2.entity.getUniqueId());
                continue;
            }
            EntityStatus status = EntityManager.get(e2.entity);
            if(status == null) continue;;
                    e2.entity.setCustomNameVisible(true);
                    String n = "§f" + e2.name + " §7" + format(status.health) + "/" + format(status.mhealth);
                    if(!n.equals(e2.entity.getName())) {
                        e2.entity.setCustomName(
                                n
                        );
                    }
        }
    }
    public String format(float n){
        if(n >= 1000000){
            return String.format("%.1fM", n / 1000000);
        }else if(n >= 1000){
            return String.format("%.1fk", n / 1000);
        }else {
            return String.format("%.0f", n);
        }
    }
}
