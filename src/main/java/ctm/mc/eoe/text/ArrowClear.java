package ctm.mc.eoe.text;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;


public class ArrowClear {
    public static void tick(){
        for(World world : Bukkit.getWorlds()){
            for(Entity entity : world.getEntities()){
                if(entity instanceof Arrow arrow){
                    if(arrow.isInBlock()){
                        arrow.remove();
                    }
                }
            }
        }
    }
}
