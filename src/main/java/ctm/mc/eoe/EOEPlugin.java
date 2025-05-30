package ctm.mc.eoe;

import ctm.mc.eoe.entities.BossManager;
import ctm.mc.eoe.event.EventListener;
import ctm.mc.eoe.scoreboardapi.LogFilter;
import ctm.mc.eoe.text.ArrowClear;
import ctm.mc.eoe.text.NametagShower;
import ctm.mc.eoe.entities.EntityManager;
import ctm.mc.eoe.tasks.M7Test;
import ctm.mc.eoe.text.TextShower;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Marker;

import java.io.File;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class EOEPlugin extends JavaPlugin {
    static Main main;
    @Override
    public void onEnable() {

        Bukkit.getLogger().info("=============================================");
        Bukkit.getLogger().info("Eclipse of Eternity 1 Plugin");
        Bukkit.getLogger().info("许可：MIT");
        Bukkit.getLogger().info("Server：" + Bukkit.getVersion());
        Bukkit.getLogger().info("=============================================");
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
        Objects.requireNonNull(this.getCommand("eoe")).setExecutor(new CommandExecutor());
        main = new Main();
        main.init();
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            EOEPlugin.get().getPM().reloadAndCalc();
            get().getCooldownManager().tick();
        }, 0, 1);
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            get().getNameTagShower().tick();
            M7Test.Instance.asyncTick();
            get().getTextShower().tick();
            EntityManager.tick();
        }, 0, 1);
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (World world : Bukkit.getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (BossManager.isOfflineBoss(entity)) {
                        BossManager.update(entity);
                    }
                }
            }
        }, 0, 10);
        Bukkit.getScheduler().runTask(this, () -> {
            for (World world : Bukkit.getWorlds())
                for (Entity entity : world.getEntities()) {
                    if (BossManager.isOfflineBoss(entity)) {
                        BossManager.addOfflineBoss(entity);
                    }
                }
        });
        // clean up

        Bukkit.getScheduler().runTask(this, ()->{
            for(Map.Entry<UUID, NametagShower.Store> i : main.nametagShower.getStores().entrySet()){
                @Nullable Entity e = Bukkit.getEntity(i.getKey());
                if(e == null) continue;
                e.remove();
            }
            for(TextShower.Store i : main.textShower.stores){
                @Nullable Entity e = Bukkit.getEntity(i.entity.getUniqueId());
                if(e == null) continue;
                e.remove();
            }
        });
    }

    @Override
    public void onDisable() {
    }

    public static Main get() {
        return main;
    }
}
