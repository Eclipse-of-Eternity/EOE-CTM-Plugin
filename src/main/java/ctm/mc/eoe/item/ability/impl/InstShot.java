package ctm.mc.eoe.item.ability.impl;

import ctm.mc.eoe.item.ability.Ability;
import ctm.mc.eoe.item.ability.Method;
import ctm.mc.eoe.skil.PlayerProfileManager;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class InstShot extends Ability {

    public InstShot() {
        super(
                "Instant shot!",
                "Shortbow",
                Method.RIGHT_CLICK
        );
    }

    @Override
    public int getManaUse() {
        return super.getManaUse();
    }

    @Override
    public float getCooldown() {
        return 0.5f;
    }

    static public Vector getVectorForRotation(float pitch, float yaw)
    {
        float f = pitch * ((float)Math.PI / 180F);
        float f1 = -yaw * ((float)Math.PI / 180F);
        float f2 = (float) Math.cos(f1);
        float f3 = (float) Math.sin(f1);
        float f4 = (float) Math.cos(f);
        float f5 = (float) Math.sin(f);
        return new Vector((double)(f3 * f4), (double)(-f5), (double)(f2 * f4));
    }

    public static Vector getUpVector(Entity entity)
    {
        return calculateUpVector(entity.getPitch(), entity.getYaw());
    }
    public static Vector getUpVector(Entity entity, float yaw)
    {
        return calculateUpVector(entity.getPitch(), (entity.getYaw() + yaw));
    }

    protected static Vector calculateUpVector(float pitch, float yaw)
    {
        return getVectorForRotation(pitch, yaw);
    }
    @Override
    public void use(PlayerProfileManager.Profile owner) {
        Vector vector = getUpVector(owner.getPlayer());
        // lol
        owner.getPlayer().playSound(owner.getPlayer(), Sound.ENTITY_ARROW_SHOOT, 0.6f, 1f);
        owner.getPlayer().getWorld().spawn(
                owner.getPlayer().getLocation().add(vector.getX() * 0.2, owner.getPlayer().getEyeHeight() + vector.getY() * 0.2, vector.getZ() * 0.2),
                Arrow.class,
                false,
                (entity)->{
                    entity.setVelocity(getUpVector(owner.getPlayer(), -5).multiply(3));
                    entity.setShooter(owner.getPlayer());
                }
        );
        owner.getPlayer().getWorld().spawn(
                owner.getPlayer().getLocation().add(vector.getX() * 0.2, owner.getPlayer().getEyeHeight() + vector.getY() * 0.2, vector.getZ() * 0.2),
                Arrow.class,
                false,
                (entity)->{
                    entity.setVelocity(getUpVector(owner.getPlayer()).multiply(3));
                    entity.setShooter(owner.getPlayer());
                }
        );
        owner.getPlayer().getWorld().spawn(
                owner.getPlayer().getLocation().add(vector.getX() * 0.2, owner.getPlayer().getEyeHeight() + vector.getY() * 0.2, vector.getZ() * 0.2),
                Arrow.class,
                false,
                (entity)->{
                    entity.setVelocity(getUpVector(owner.getPlayer(), 5).multiply(3));
                    entity.setShooter(owner.getPlayer());
                }
        );
    }
}
