package ctm.mc.eoe.event;

import io.papermc.paper.event.player.PlayerArmSwingEvent;
import ctm.mc.eoe.EOEPlugin;
import ctm.mc.eoe.entities.EntityManager;
import ctm.mc.eoe.item.ability.Ability;
import ctm.mc.eoe.item.ability.Method;
import ctm.mc.eoe.item.base.ItemBase;
import ctm.mc.eoe.item.base.ItemType;
import ctm.mc.eoe.item.player.EntityStatus;
import ctm.mc.eoe.skil.PlayerProfileManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;
import java.util.Random;

import static org.bukkit.event.block.Action.RIGHT_CLICK_AIR;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;

public class EventListener implements Listener {

    @EventHandler
    public void onAttack(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            EntityStatus status = EntityManager.get(event.getEntity());
            if (status == null) return;
            // 模组生物
            LivingEntity e = (LivingEntity) event.getEntity();
            status.health = Math.max((float) (status.health - event.getDamage()), 0);
            double health = e.getMaxHealth() * (status.health / status.mhealth);
            if (status.health != health) {
                e.setHealth(health);
            }
        }
    }
    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player)
            return;

        if (!(event.getDamager() instanceof Player) && !(event.getDamager() instanceof Arrow && ((Arrow) event.getDamager()).getShooter() instanceof Player))
            return;

        // TODO: 获取玩家
        Player player = event.getDamager() instanceof Player ? (Player) event.getDamager() : (Player) ((Arrow) event.getDamager()).getShooter();

        // TODO: 获取玩家信息
        EntityStatus status = EntityManager.get(event.getEntity());

//        // TODO: setCancelled
//        event.setCancelled(true);
        // TODO: 有效伤害
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)
            return;

        PlayerProfileManager.Profile profile;
        if (player != null) {
            profile = EOEPlugin.get().getPM().get(player.getUniqueId());
        } else return;

        // 是否写入
        if (profile == null) return;

        if (event.getDamager() instanceof Arrow) {
            event.getDamager().remove();
            player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.8f, 1);
        }else if (profile.getHandItem() != null && profile.getHandItem().getStatus().getItemType() == ItemType.Bow) {
            return;
        }
        // TODO: 触发事件
        if (profile.getHandItem() != null) {
            profile.getHandItem().onAttack(profile, event.getEntity());
            for (Ability ability : profile.getHandItem().getStatus().getAbilities()) {
                if (ability.getMethod() != Method.ATTACK) continue;
                useAbility(profile, ability);
            }
        }

        // TODO: 检测暴击
        boolean critical = event.isCritical();

        // TODO: 特殊攻击

        // TODO: 计算攻击
        double actDmg = event.getDamage();

        if (status != null) {
            // 模组生物
            LivingEntity e = (LivingEntity) event.getEntity();
            status.health = Math.max((float) (status.health - actDmg), 0);
            double health = e.getMaxHealth() * (status.health / status.mhealth);
            if (status.health != health) {
                e.setHealth(health);
            }
            if(status.health <= 0){
                e.setHealth(0);
            }
            event.setDamage(0);
        }
        // TODO: 显示伤害
        EOEPlugin.get().getTextShower().showDamage((float) actDmg, event.getEntity(), critical);
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
//        Bukkit.getLogger().info("INTERACT : " + event.getAction().name() + " | " + event.hasItem());
        if ((event.getAction() == RIGHT_CLICK_AIR || event.getAction() == RIGHT_CLICK_BLOCK) && event.hasItem()) {
//            Bukkit.getLogger().info("INTERACT 1");
            Player player = event.getPlayer();
            PlayerProfileManager.Profile profile = EOEPlugin.get().getPM().get(player.getUniqueId());
            // 是否写入
            if (profile == null) return;
            if (profile.getCooldowns().useItemCooldown == 0) {
                profile.getCooldowns().useItemCooldown = 1;
                ItemBase itemBase = profile.getHandItem();
//                Bukkit.getLogger().info("INTERACT");
                if (itemBase != null) {
                    itemBase.onUse(profile, event);
                    for (Ability ability : itemBase.getStatus().getAbilities()) {
                        if (ability.getMethod() != Method.RIGHT_CLICK) continue;
                        useAbility(profile, ability);
                    }
                }
            }
        }
    }

//    @EventHandler
//    public void onSwing(PlayerArmSwingEvent event) {
//        Player player = event.getPlayer();
//        PlayerProfileManager.Profile profile = EOEPlugin.get().getPM().get(player.getUniqueId());
//        // 是否写入
//        if (profile == null) return;
//        ItemBase itemBase = profile.getHandItem();
//        if (itemBase != null) {
//            for (Ability ability : itemBase.getStatus().getAbilities()) {
//                if (ability.getMethod() != Method.SWING) continue;
//                useAbility(profile, ability);
//            }
//        }
//    }

    public void useAbility(PlayerProfileManager.Profile profile, Ability ability) {
        if (profile.mana >= ability.getManaUse()) {
            if (EOEPlugin.get().getCooldownManager().isCooldownOK(profile, "", ability)) {
                EOEPlugin.get().getCooldownManager().resetCooldown(profile, "", ability);
                profile.mana -= ability.getManaUse();
                profile.sendMana();
                ability.use(profile);
                if (ability.getManaUse() != 0)
                    profile.getPlayer().sendMessage(
                            Component.translatable("eoe.use_ability").style(Style.empty().color(TextColor.color(new Color(133, 133, 133).getRGB())))
                                    .append(Component.text(" §8" + ability.getName())).style(Style.empty().color(TextColor.color(new Color(67, 236, 197).getRGB())))
                                    .append(Component.text("§8 (-" + ability.getManaUse() + " Mana)")).style(Style.empty().color(TextColor.color(new Color(255, 150, 48).getRGB())))
                    );
            }
        } else {
            profile.getPlayer().sendMessage(Component.translatable("eoe.no_mana").style(Style.empty().color(TextColor.color(
                    new Color(255, 78, 78).getRGB()
            ))));
        }
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        EOEPlugin.get().getPM().addProfile(event.getPlayer());
    }

    @EventHandler
    public void exit(PlayerQuitEvent event) {
        EOEPlugin.get().getPM().removeProfile(event.getPlayer());
    }
}
