package ctm.mc.eoe.skil;

import com.google.common.collect.Lists;
import ctm.mc.eoe.item.base.ItemBase;
import ctm.mc.eoe.item.player.Cooldowns;
import ctm.mc.eoe.scoreboardapi.ScoreboardAPI;
import lombok.Getter;
import ctm.mc.eoe.nbt.NBTReader;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class PlayerProfileManager {
    @Getter
    public static class Profile {
        UUID uuid;
        Player player;
        Cooldowns cooldowns;
        ItemBase handItem;
        public int mana;
        public int maxMana;
        public float additionManaRegen;
        public Profile(Player uuid){
            this.uuid = uuid.getUniqueId();
            this.player = uuid;
            this.cooldowns = new Cooldowns();
            this.mana = 100;
            this.maxMana = 100;
            this.additionManaRegen = 0;
        }
        public void sendMana(){
            ScoreboardAPI.setScore(
                    player,
                    "mana",
                    this.mana
            );
//            Objects.requireNonNull(Bukkit.getScoreboardManager().getMainScoreboard().getObjective("mana")).getScoreFor(player).setScore(this.mana);
        }
        public void setHandItem(ItemBase handItem) {
            this.handItem = handItem;
        }

    }
    public HashMap<UUID, Profile> profileMap = new HashMap();
    public void addProfile(Player player){
        profileMap.put(player.getUniqueId(), new Profile(player));
    }
    public void removeProfile(Player player){
        profileMap.remove(player.getUniqueId());
    }
    public Profile get(Player player){
        return profileMap.get(player.getUniqueId());
    }
    public Profile get(UUID player){
        return profileMap.get(player);
    }
    int addMana = 0;
    int reCalc = 0;
    public void reloadAndCalc(){
        addMana ++;
        reCalc ++;
        for(Map.Entry<UUID, Profile> uuid : profileMap.entrySet()){
            Player player = uuid.getValue().player;

            ItemBase itemBase = NBTReader.readFromStack(player.getItemInHand());

            uuid.getValue().getCooldowns().useItemCooldown --;
            uuid.getValue().getCooldowns().useItemCooldown = Math.max(uuid.getValue().getCooldowns().useItemCooldown, 0);
            uuid.getValue().setHandItem(itemBase);
            if(reCalc >= 5) {
                float additionManaRegen = 0;
                float additionMana = 0;
                ItemStack[] itemStacks = new ItemStack[]{
                        player.getInventory().getArmorContents()[0],
                        player.getInventory().getArmorContents()[1],
                        player.getInventory().getArmorContents()[2],
                        player.getInventory().getArmorContents()[3],
                        player.getItemInHand()
                };
                for (ItemStack itemStack : itemStacks) {
                    ItemBase item = NBTReader.readFromStack(itemStack);
                    if (item != null) {
                        additionManaRegen += item.getTags().mana_regen;
                        additionMana += item.getTags().mana;
                    }
                }
                uuid.getValue().maxMana = (int) (100 + additionMana);
                uuid.getValue().additionManaRegen = additionManaRegen;
            }
            ScoreboardAPI.setScore(
                    player,
                    "mana",
                    uuid.getValue().mana
            );
            ScoreboardAPI.setScore(
                    player,
                    "maxMana",
                    uuid.getValue().maxMana
            );
//            Objects.requireNonNull(Bukkit.getScoreboardManager().getMainScoreboard().getObjective("mana")).getScoreFor(player).setScore(uuid.getValue().mana);
//            Objects.requireNonNull(Bukkit.getScoreboardManager().getMainScoreboard().getObjective("maxMana")).getScoreFor(player).setScore(uuid.getValue().maxMana);
//            Bukkit.getConsoleSender().sendMessage(
//                    ""
//            );
            if(addMana >= 20){
                uuid.getValue().mana += (int) (uuid.getValue().maxMana * (0.05 * (1 + uuid.getValue().additionManaRegen)));
                uuid.getValue().mana = Math.min(uuid.getValue().mana, uuid.getValue().maxMana);
            }
        }
        if(addMana >= 20) {
            addMana = 0;
        }
        if(reCalc >= 5) {
            reCalc = 0;
        }
    }
}
