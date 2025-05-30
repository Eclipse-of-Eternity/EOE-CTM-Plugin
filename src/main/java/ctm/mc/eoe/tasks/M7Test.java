package ctm.mc.eoe.tasks;

import ctm.mc.eoe.utils.Utils;
import ctm.mc.eoe.entities.BossManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;

public class M7Test {
    public static M7Test Instance = new M7Test();
    int tick = 0;
    boolean sync = false;
    World world = null;
    private void sleep(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
        }
    }
    ArrayList<Runnable> syncList = new ArrayList<>();
    public void addToSync(Runnable runnable){
        this.syncList.add(runnable);
    }
    public void syncClear(){
        this.syncList.forEach(Runnable::run);
        this.syncList.clear();
    }
    public void spawn(World world){
        if(sync) return;
        tick = 0;
        this.world = world;
        sync = true;
        new Thread(()->{
            sleep(8000);
            addToSync(()-> Utils.sendMSG(witherKingPrefix + "Awakening me from my slumber, what a strange choice."));
            sleep(1000);
            addToSync(()->Utils.sendMSG(witherKingPrefix + "I have nothing left to fight for, I finally had peace."));
            sleep(1000);
            addToSync(()->Utils.sendMSG(witherKingPrefix + "Mortals fighting against supreme beings, what a moving spectacle."));
            sleep(1000);
            addToSync(()->Utils.sendMSG(witherKingPrefix + "My enemies are now my strongest allies!"));
            sleep(1000);
            addToSync(()->Utils.sendMSG("§d§lSoul Dragon §7is spawning!"));
            addToSync(()->Utils.sendMSG("§a§lApex Dragon §7is spawning!"));
            for(int i = 0 ; i < 3 ; i ++) {
                sleep(1000);
                assert EntityType.LIGHTNING.getEntityClass() != null;
                addToSync(()->world.spawn(new Location(world, 56, 8, 126), EntityType.LIGHTNING.getEntityClass())); // soul
                addToSync(()->world.spawn(new Location(world, 26, 6, 94), EntityType.LIGHTNING.getEntityClass())); // apex
            }
            sleep(1000);
            addToSync(()-> BossManager.spawn(new Location(world,56,19,124), "dragon1"));
            addToSync(()-> BossManager.spawn(new Location(world,26,18,94), "dragon2"));

            int killtime = 15 * 1000;
            sleep(killtime);
            addToSync(()->Utils.sendMSG(witherKingPrefix + "Oh, this one hurts!"));
            sleep(500);
            addToSync(()->Utils.sendMSG("§6§lFlame Dragon §7is spawning!"));
            for(int i = 0 ; i < 3 ; i ++) {
                sleep(1000);
                assert EntityType.LIGHTNING.getEntityClass() != null;
                addToSync(()->world.spawn(new Location(world,86,6,56), EntityType.LIGHTNING.getEntityClass())); // flame
            }
            sleep(1000);
            addToSync(()-> BossManager.spawn(new Location(world, 86, 17, 56), "dragon3"));

            sleep(killtime);
            addToSync(()->Utils.sendMSG(witherKingPrefix + "I have more of those."));
                sleep(500);
            addToSync(()->Utils.sendMSG("§c§lPower Dragon §7is spawning!"));

            for(int i = 0 ; i < 3 ; i ++) {
                sleep(1000);
                assert EntityType.LIGHTNING.getEntityClass() != null;
                addToSync(()->world.spawn(new Location(world,26,6,59), EntityType.LIGHTNING.getEntityClass())); // flame
            }
            sleep(1000);
            addToSync(()-> BossManager.spawn(new Location(world,26,17,59), "dragon4"));

            sleep(killtime);
            addToSync(()->Utils.sendMSG(witherKingPrefix + "My soul is disposable."));
            sleep(500);
            addToSync(()->Utils.sendMSG("§b§lIce Dragon §7is spawning!"));
            for(int i = 0 ; i < 3 ; i ++) {
                sleep(1000);
                assert EntityType.LIGHTNING.getEntityClass() != null;
                addToSync(()->world.spawn(new Location(world,85,6,94), EntityType.LIGHTNING.getEntityClass())); // flame
            }
            sleep(1000);
            addToSync(()-> BossManager.spawn(new Location(world,85,17,94), "dragon5"));
            sleep(killtime);
            addToSync(()->Utils.sendMSG(witherKingPrefix + "Incredible. You did what I couldn't do myself."));
            sleep(1000);
            addToSync(()->Utils.sendMSG(witherKingPrefix + "In a way I should thank you, I lost all hopes centuries ago that it would ever end."));
            sleep(1000);
            addToSync(()->Utils.sendMSG(witherKingPrefix + "My strengths are depleting, this… this is it…"));
            sync = false;

        }).start();
    }
    String witherKingPrefix = "§0§k[BOSS] Wither King: §c";
    public void asyncTick(){
        syncClear();
    }

}
