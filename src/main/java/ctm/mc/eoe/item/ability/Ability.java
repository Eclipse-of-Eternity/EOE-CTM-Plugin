package ctm.mc.eoe.item.ability;

import ctm.mc.eoe.skil.PlayerProfileManager;

public abstract class Ability {
    String desc, name;
    Method method;
    int totalCooldown;
    public Ability(String desc, String name, Method method){
        this.desc = desc;
        this.name = name;
        this.method = method;
        this.totalCooldown = 0;
    }

    public int getManaUse() {
        return 0;
    }
    public float getCooldown() {
        return 0;
    }

    public abstract void use(PlayerProfileManager.Profile owner);

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public Method getMethod() {
        return method;
    }

    public int getTotalCooldown() {
        return totalCooldown;
    }

    public void setTotalCooldown(int totalCooldown) {
        this.totalCooldown = totalCooldown;
    }
}
