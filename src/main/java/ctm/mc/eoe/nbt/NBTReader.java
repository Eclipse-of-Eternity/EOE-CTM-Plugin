package ctm.mc.eoe.nbt;

import ctm.mc.eoe.EOEPlugin;
import ctm.mc.eoe.enchantments.Enchantments;
import ctm.mc.eoe.item.ability.Ability;
import ctm.mc.eoe.item.base.ItemBase;
import ctm.mc.eoe.item.base.StatTags;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.handler.NBTHandlers;
import de.tr7zw.nbtapi.iface.NBTHandler;
import de.tr7zw.nbtapi.iface.ReadWriteItemNBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBTList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NBTReader {
    public static ItemBase readFromStack(ItemStack itemStack) {
        if (itemStack == null) return null;
        if (itemStack.getItemMeta() == null) return null;
        ReadWriteNBT entityNbt = NBT.createNBTObject();
        NBT.get(itemStack, entityNbt::mergeCompound);

//        net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(itemStack); // Convert the Bukkit ItemStack to a NMS ItemStack.
//        NBTTagCompound tag = stack.w(); // Get the item tag.
        String itemID = entityNbt.getString("item_id");
        if (itemID.isEmpty()) return null;
        String itemUuID = entityNbt.getString("item_uuid");
        if (itemUuID.isEmpty()) return null;

        ItemBase base = EOEPlugin.get().getItemManager().getItem(itemID).clone();
        if (base == null) return null;
        float mana = entityNbt.getFloat("mana");
        float mana_boost = entityNbt.getFloat("mana_regen");
        base.setTags(new StatTags(mana, mana_boost));
        Enchantments enchantments = new Enchantments(entityNbt.getInteger("max_enchantments"));
        enchantments.enchantments = itemStack.getEnchantments();
        base.setEnchantments(enchantments);
        return base;
    }

    public static ItemStack readFromBase(ItemBase base) {
        ItemStack itemStack = base.baseStack();
        NBT.modify(itemStack, nbt -> {
            nbt.setString("item_id", base.getID());
            nbt.setString("item_uuid", UUID.randomUUID().toString());

            nbt.setFloat("mana", base.getTags().mana);
            nbt.setFloat("mana_regen", base.getTags().mana_regen);

            nbt.setInteger("max_enchantments", base.getEnchantments().enchantmentsAmount);
        });
        itemStack.addEnchantments(base.getEnchantments().enchantments);
//        ItemMeta itemMeta = itemStack.getItemMeta();
//        itemMeta.displayName(base.getStatus().getName());
        @Nullable List<TextComponent> strs;
        strs = new ArrayList<>();
        TextComponent component = Component.empty().style(Style.empty()).append(
                Component.text("⭐ ").append(Component.translatable("eoe.ench.slot")).color(TextColor.color(new Color(110, 110, 255).getRGB())));
        List<Enchantment> enchantmentsList = base.getEnchantments().enchantments.keySet().stream().toList();
        for (int i = 0; i < base.getEnchantments().enchantmentsAmount; i++) {
            Enchantment enchantment = null;
            if (enchantmentsList.size() > i) {
                enchantment = enchantmentsList.get(i);
            }
            component = component.append(Component.text(" §7[ "))
                    .append(enchantment == null ?
                            (Component.translatable("eoe.ench.empty")).color(TextColor.color(new Color(164, 164, 164).getRGB())) :
                            (Component.translatable(enchantment.translationKey()).append(Component.text(" " + base.getEnchantments().enchantments.get(enchantment))))
                    )
                    .append(Component.text(" §7]"));
        }
        strs.add(Component.empty().style(Style.empty()).append(
                        Component.text("⭕ ").append(Component.translatable("eoe.upgrade.slot")).color(TextColor.color(new Color(255, 142, 65).getRGB()))
                        )
                .append(Component.text(" §7[ "))
                .append(Component.translatable("eoe.ench.empty")).color(TextColor.color(new Color(164, 164, 164).getRGB()))
                .append(Component.text(" §7]"))
        ); // line
        strs.add(Component.empty().style(Style.empty()).append(
                        Component.translatable("eoe.ability.slot").color(TextColor.color(new Color(176, 166, 0).getRGB()))
                        )
                        .append(Component.text(" §7[ "))
                        .append(Component.translatable("eoe.ench.empty")).color(TextColor.color(new Color(164, 164, 164).getRGB()))
                        .append(Component.text(" §7]"))
        ); // line
        strs.add(component);
        if (base.getTags().mana != 0) {
            strs.add(Component.text("§9Mana: §7+" + base.getTags().mana));
        }
        if (base.getTags().mana_regen != 0) {
            strs.add(Component.text("§9⚡ Mana Regen: §7+" + (int) (base.getTags().mana_regen * 100) + "%"));
        }
        strs.add(Component.text("")); // line
        for (Ability ability : base.getStatus().getAbilities()) {
            strs.add(Component.text("§6Ability: " + ability.getName() + " §e§l" + ability.getMethod().getDesc()));
            if (ability.getDesc().length() < 15) {
                strs.add(Component.text("§7" + ability.getDesc()));
            } else {
                for (String str : ability.getDesc().split("\n")) {
                    strs.add(Component.text("§7" + str));
                }
            }
            if (ability.getManaUse() != 0)
                strs.add(Component.text("§8Mana Cost: §b" + ability.getManaUse()));
            if (ability.getCooldown() != 0)
                strs.add(Component.text("§8Cooldown: §c" + ability.getCooldown() + "s"));
        }
        NBT.modify(itemStack, (nbt) -> {
            nbt.modifyMeta((readOnlyNbt, meta) -> { // Do not modify the nbt while modifying the meta!
                meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS);
                meta.displayName(base.getStatus().getName());
                meta.lore(strs);
            });
        });
        return itemStack;
    }

    private static String parseFrom(String pre, String str) {
        return str.substring(pre.length()).split(" ")[0];
    }

    private static String parseFromWithPrefix(String pre) {
        return pre.substring(0, pre.length() - 1);
    }
}
