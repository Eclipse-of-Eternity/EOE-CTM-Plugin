package ctm.mc.eoe.text;

import ctm.mc.eoe.utils.TimerUtil;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Random;


public class TextShower {
    public static class Store {
        TimerUtil timerUtil = new TimerUtil();
        public Entity entity;
        public Store(Entity entity){
            this.entity = entity;
        }
    }
    public ArrayList<Store> stores = new ArrayList<>();
    Random random = new Random();
    public void showText(String s, Location location){
        if (EntityType.ARMOR_STAND.getEntityClass() != null) {
            stores.add(new Store(location.getWorld().spawn(location, EntityType.ARMOR_STAND.getEntityClass(), false, (entity)-> {
                entity.setGravity(false);
                entity.setInvulnerable(true);
                ((ArmorStand)entity).setMarker(true);
                ((ArmorStand)entity).setInvisible(true);
                entity.setCustomNameVisible(true);
                entity.setCustomName(s);
            })));
        }
    }
    private String format(boolean crit, float dmg){
        String str = String.format("%.0f", dmg);
        StringBuilder str2 = new StringBuilder();
        if(crit) str2.append("⭐");
        int u = 0;
        for(int i = str.length() - 1; i >= 0 ; i--){
            u ++;
            if(u == 3 && i != 0){
                u = 0;
                str2.append(str.charAt(i)).append(",");
            }else {
                str2.append(str.charAt(i));
            }
        }
        if(crit) str2.append("⭐");
        String critF = "e6f";
        String app = str2.toString();
        str2 = new StringBuilder();
        if(!crit) str2.append("§7");
        for(int i = app.length() - 1; i >= 0 ; i--){
            if(crit) {
                str2.append("§").append(critF.charAt(i % 3)).append(app.charAt(i));
            }else{
                str2.append(app.charAt(i));
            }
        }
        return str2.toString();
    }
    public void showDamage(float dmg, Entity player, boolean crit){
        showText(format(crit, dmg), player.getLocation().add(
                (random.nextInt(100) - 50) / 150f,
                (random.nextInt(100) - 50) / 150f + 1,
                (random.nextInt(100) - 50) / 150f
        ));
    }

    public void tick() {
        for (Store e2 : stores.toArray(new Store[0])) {
            if(stores.size() > 100 || e2.timerUtil.hasTimeElapsed(2000) || e2.entity == null){
                if(e2.entity != null && e2.entity.getLocation().isChunkLoaded()){
                    e2.entity.remove();
                }
                stores.remove(e2);
            }
        }
    }
}
