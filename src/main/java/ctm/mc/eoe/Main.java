package ctm.mc.eoe;

import ctm.mc.eoe.item.ItemManager;
import ctm.mc.eoe.text.CooldownManager;
import ctm.mc.eoe.text.NametagShower;
import ctm.mc.eoe.text.TextShower;
import lombok.Getter;
import ctm.mc.eoe.skil.PlayerProfileManager;
import ctm.mc.eoe.entities.BossManager;

public class Main {
    @Getter
    ItemManager itemManager;
    PlayerProfileManager playerProfileManager;
    @Getter
    TextShower textShower;
    @Getter
    CooldownManager cooldownManager;
    NametagShower nametagShower;
    public void init(){
        itemManager = new ItemManager();
        itemManager.init();
        playerProfileManager = new PlayerProfileManager();
        textShower = new TextShower();
        cooldownManager = new CooldownManager();
        nametagShower = new NametagShower();
        BossManager.load();
    }

    public NametagShower getNameTagShower() {
        return nametagShower;
    }

    public PlayerProfileManager getPM() {
        return playerProfileManager;
    }
}
