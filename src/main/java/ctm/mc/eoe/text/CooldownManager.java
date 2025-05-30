package ctm.mc.eoe.text;

import ctm.mc.eoe.utils.TimerUtil;
import ctm.mc.eoe.item.ability.Ability;
import ctm.mc.eoe.skil.PlayerProfileManager;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;


public class CooldownManager {
    private static class Store {
        TimerUtil timerUtil = new TimerUtil();
        PlayerProfileManager.Profile entity;
        String abilityID;
        String uuid;
        Ability ability;
        public Store(PlayerProfileManager.Profile entity, String uuid, Ability ability)
        {
            this.entity = entity;
            this.abilityID = ability.hashCode() + ";";
            this.uuid = uuid;
            this.ability = ability;
        }
    }
    HashMap<UUID, HashMap<String, Store>> stores = new HashMap<>();
    Random random = new Random();
    public void resetCooldown(PlayerProfileManager.Profile profile, String uuid, Ability ability){
        if(ability.getCooldown() == 0 || !isCooldownOK(profile, uuid, ability)) return;
        stores.computeIfAbsent(profile.getPlayer().getUniqueId(), k -> new HashMap<>()).put(ability.getName(), new Store(profile, uuid, ability));
    }
    public boolean isCooldownOK(PlayerProfileManager.Profile profile, String uuid, Ability ability){
        if(ability.getCooldown() == 0) return true;
        HashMap<String, Store> store = stores.get(profile.getPlayer().getUniqueId());
        if(store == null || store.get(ability.getName()) == null) return true;
        float as = 1;
//        if(profile.getHandItem() instanceof ItemStat weaponItem){
//            as = 1 + weaponItem.getWeaponStatus().attackSpeed;
//        }
        return store.get(ability.getName()).timerUtil.hasTimeElapsed((ability.getCooldown() / as) * 1000L);
    }
    public void tick() {
        UUID[] uuids = stores.keySet().toArray(new UUID[0]);;
        int i = 0;
        for (HashMap<String, Store> e2 : stores.values().toArray(new HashMap[0])) {
            for(Store store : e2.values().toArray(new Store[0])){
                if(store.timerUtil.hasTimeElapsed(1000L * store.ability.getCooldown())){
                    e2.remove(store.ability.getName());
                }
            }
            if(e2.isEmpty()){
                stores.remove(uuids[i]);
            }
            i ++;
        }
    }
}
