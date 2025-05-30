package ctm.mc.eoe.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@Getter
@Setter
public class SBPlayerGetAttackedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    protected float damage;
    protected Player player;
    protected Cause cause;
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public enum Cause {
        DAMAGE,
        MAGIC_DAMAGE,
        TRUE_DAMAGE
    }
}