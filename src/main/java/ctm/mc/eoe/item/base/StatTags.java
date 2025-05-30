package ctm.mc.eoe.item.base;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StatTags implements Cloneable {
    public float mana;
    public float mana_regen;

    @Override
    public StatTags clone() {
        try {
            StatTags clone = (StatTags) super.clone();
            clone.mana = mana;
            clone.mana_regen = mana_regen;
            // TODO: 复制此处的可变状态，这样此克隆就不能更改初始克隆的内部项
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
