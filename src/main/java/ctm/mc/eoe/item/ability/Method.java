package ctm.mc.eoe.item.ability;

import lombok.Getter;

@Getter
public enum Method {
    RIGHT_CLICK("RIGHT CLICK"),
//    SWING("LEFT CLICK"),
    SNEAK("SNEAK"),
    ON_HAND(""),
    ATTACK("");
    final String desc;
    Method(String desc){
        this.desc = desc;
    }

}
