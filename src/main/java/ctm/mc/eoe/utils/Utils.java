package ctm.mc.eoe.utils;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class Utils {
    public static String LOCK_STR = "§c地图已经被锁定了 无法操作 所有者: §f";
    public static String LOCK_DATA = "§0§kUUID:";
    public static class LockedMap {
        String uuid;
        boolean isLock;
        boolean isMap;
        public LockedMap(String uuid, boolean isLock, boolean isMap){
            this.uuid = uuid;
            this.isLock = isLock;
            this.isMap = isMap;
        }
    }
    public static void sendMSG(String s){
        for(World world : Bukkit.getWorlds()){
            for(Player player : world.getPlayers()){
                player.sendMessage(s);
            }
        }
    }
    @SuppressWarnings("deprecation")
    public static LockedMap isLockedMap(ItemStack itemStack){
        if (itemStack == null || !(itemStack.getType() == Material.FILLED_MAP))
            return new LockedMap(null, false, false);
        if (itemStack.getItemMeta() == null || itemStack.getItemMeta().getLore() == null)
            return new LockedMap(null, false, true);
        boolean has = false;
        String owner = "";
        for (String str : itemStack.getItemMeta().getLore()) {
            if(str.startsWith(LOCK_STR)){
                has = true;
            }
            if(str.startsWith(LOCK_DATA)){
                owner = str.substring(LOCK_DATA.length());
                break;
            }
        }
        return new LockedMap(owner, has && !owner.isEmpty(), true);
    }
    @SuppressWarnings("deprecation")
    public static ItemStack lockedMap(Player owner, @Nonnull ItemStack itemStack) {
        ItemMeta data = itemStack.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add(LOCK_STR + owner.getName());
        lore.add(LOCK_DATA + owner.getUniqueId());
        data.setLore(lore);
        data.setDisplayName((data.getDisplayName().isEmpty() ? "地图" : data.getDisplayName()) + " §6(拥有者: " + owner.getName() + ")");
        itemStack.setItemMeta(data);
        return itemStack;
    }
    @SuppressWarnings("deprecation")
    public static ItemStack unlockedMap(Player owner, @Nonnull ItemStack itemStack) {
        ItemMeta data = itemStack.getItemMeta();
        data.setLore(new ArrayList<>());
        data.setDisplayName(data.getDisplayName().contains(" §6") ? "地图" : data.getDisplayName().split(" §6")[0]);
        itemStack.setItemMeta(data);
        return itemStack;
    }
}
