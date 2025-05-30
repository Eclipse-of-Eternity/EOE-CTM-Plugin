package ctm.mc.eoe.item;

import ctm.mc.eoe.item.base.ItemBase;
import ctm.mc.eoe.item.impl.RogueSwordItem;

import java.util.HashMap;

public class ItemManager {
    HashMap<String, ItemBase> itemBaseHashMap = new HashMap<>();

    public ItemBase getItem(String string) {
        ItemBase b = itemBaseHashMap.get(string);
        itemBaseHashMap.clear();
        init();
        return b;
    }
    private void registerItem(String id, ItemBase itemBase){
        itemBase.init();
        itemBaseHashMap.put(id, itemBase);
    }
    public void init(){
        registerItem("ROGUE_SWORD", new RogueSwordItem());
    }
}
