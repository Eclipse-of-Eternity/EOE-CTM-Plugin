package ctm.mc.eoe.item.base;

import lombok.Getter;
import ctm.mc.eoe.item.ability.Ability;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;

@Getter
public class Status implements Cloneable {
    TextComponent name;
    ItemType itemType;
    ArrayList<Ability> abilities = new ArrayList<>();
    public Status name(TextComponent v1){
        this.name = v1;
        return this;
    }
    public Status type(ItemType v1){
        this.itemType = v1;
        return this;
    }
    public Status addAbility(Ability v1){
        this.abilities.add(v1);
        return this;
    }
    public Status addAbilities(ArrayList<Ability> v1){
        this.abilities.clear();
        this.abilities.addAll(v1);
        return this;
    }

    @Override
    public Status clone() {
        try {
            Status clone = (Status) super.clone();
            clone.name = name;
            clone.itemType = itemType;
            clone.abilities = abilities;
            // TODO: 复制此处的可变状态，这样此克隆就不能更改初始克隆的内部项
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
