package ctm.mc.eoe.enchantments;

import ctm.mc.eoe.item.base.ItemBase;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Enchantments implements Cloneable {
    public Map<Enchantment, Integer> enchantments;
    public int enchantmentsAmount;
    public Enchantments(int amount){
        this.enchantments = new HashMap<>();
        this.enchantmentsAmount = amount;
    }

    @Override
    public Enchantments clone() {
        try {
            Enchantments clone = (Enchantments) super.clone();
            clone.enchantments = new HashMap<>(enchantments);
            clone.enchantmentsAmount = enchantmentsAmount;
            // TODO: 复制此处的可变状态，这样此克隆就不能更改初始克隆的内部项
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
