package ctm.mc.eoe.item.impl;

import ctm.mc.eoe.enchantments.Enchantments;
import ctm.mc.eoe.item.ability.impl.SpeedBoost;
import ctm.mc.eoe.item.base.ItemBase;
import ctm.mc.eoe.item.base.ItemType;
import ctm.mc.eoe.item.base.Status;
import ctm.mc.eoe.item.base.StatTags;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class RogueSwordItem extends ItemBase {
    {
        this.tags = new StatTags(0,0);
        this.name = "ROGUE_SWORD";
        this.enchantments = new Enchantments(2);
    }
    @Override
    public Status getStat() {
        return new Status().name(Component.text("Rogue Sword")).type(ItemType.Sword).addAbility(new SpeedBoost());
    }

    @Override
    public ItemStack baseStack() {
        return new ItemStack(Material.GOLDEN_SWORD);
    }
}
