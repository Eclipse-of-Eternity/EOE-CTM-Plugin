package ctm.mc.eoe.item.base;

import java.awt.*;

public enum ItemType {
    Sword("Sword"),
    Pickaxe("Pickaxe"),
    Boots("Boots"),
    Leggings("Leggings"),
    Chestplate("Chestplate"),
    Helmet("Helmet"),
    Bow("Bow"),
    Axe("Axe")
    ;
    final String name;
    ItemType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
