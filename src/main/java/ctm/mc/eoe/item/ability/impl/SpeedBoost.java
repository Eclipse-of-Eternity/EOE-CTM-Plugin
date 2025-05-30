package ctm.mc.eoe.item.ability.impl;

import ctm.mc.eoe.item.ability.Ability;
import ctm.mc.eoe.item.ability.Method;
import ctm.mc.eoe.skil.PlayerProfileManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedBoost extends Ability {

    public SpeedBoost() {
        super("Gain Speed I for 5s.", "Speed UP!", Method.RIGHT_CLICK);
    }

    @Override
    public int getManaUse() {
        return 20;
    }

    @Override
    public float getCooldown() {
        return 5;
    }

    @Override
    public void use(PlayerProfileManager.Profile owner) {
        owner.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20, 0, false, false, true));
    }
}
