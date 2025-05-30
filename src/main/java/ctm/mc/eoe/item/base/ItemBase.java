package ctm.mc.eoe.item.base;

import ctm.mc.eoe.enchantments.Enchantments;
import lombok.Getter;
import lombok.Setter;
import ctm.mc.eoe.skil.PlayerProfileManager;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
@Getter
@Setter

public abstract class ItemBase implements Cloneable {
    protected String name;
    protected Status status;
    protected StatTags tags;
    protected Enchantments enchantments;
    public void init(){
        this.status = getStat();
    }
    public void onUse(PlayerProfileManager.Profile owner, PlayerInteractEvent event) {
    }
    public void onAttack(PlayerProfileManager.Profile owner, Entity target) {
    }
    protected abstract Status getStat();
    public abstract ItemStack baseStack();
    public String getID() {
        return name;
    }
    @Override
    public ItemBase clone() {
        try {
            ItemBase clone = (ItemBase) super.clone();
            clone.status = status.clone();
            clone.name = name;
            clone.tags = tags.clone();
            clone.enchantments = enchantments.clone();
            // TODO: 复制此处的可变状态，这样此克隆就不能更改初始克隆的内部项
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
