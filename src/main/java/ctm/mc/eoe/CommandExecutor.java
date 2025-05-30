package ctm.mc.eoe;

import ctm.mc.eoe.item.base.ItemBase;
import ctm.mc.eoe.nbt.NBTReader;
import ctm.mc.eoe.entities.BossManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {

        public CommandExecutor() {
        }

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
                if (cmd.getName().equalsIgnoreCase("eoe")) {
                        if (!sender.hasPermission("eoe.permission.all")) return false;
                        if (!(sender instanceof Player player)) return false;
                        if(args.length == 0){
                                player.sendMessage("EOE Plugin");
                                player.sendMessage("/eoe item [ITEM_ID] - 获取物品");
                                player.sendMessage("/eoe boss [BOSS_ID] - 召唤Boss");
                                player.sendMessage("/eoe editNBT [name] [value] - 修改物品NBT");
                                return true;
                        }else if(args[0].equals("item") && args.length == 2){
                                ItemBase itemBase = EOEPlugin.get().itemManager.getItem(args[1]);
                                if(itemBase == null){
                                        player.sendMessage("没有找到: " + args[1]);
                                        return true;
                                }
                                player.getInventory().addItem(NBTReader.readFromBase(itemBase));
                                player.sendMessage("成功获取！");
                                return true;
                        }else if(args[0].equals("boss") && args.length == 2){
                                if(!BossManager.hasBoss(args[1])){
                                        player.sendMessage("没有找到: " + args[1]);
                                        return true;
                                }
                                BossManager.spawn(player.getLocation(), args[1]);
                                player.sendMessage("Done！");
                                return true;
                        }else if(args[0].equals("editNBT") && args.length == 3) {
                                ItemStack itemStack = player.getItemInHand();
                                ItemBase base = NBTReader.readFromStack(itemStack);
                                if (base != null) {
                                        try {
                                                switch (args[1]) {
                                                        case "mana":
                                                                base.getTags().mana = Float.parseFloat(args[2]);
                                                                break;
                                                        case "mana_regen":
                                                                base.getTags().mana_regen = Float.parseFloat(args[2]);
                                                                break;
                                                }
                                        } catch (Exception e) {
                                                e.printStackTrace();
                                                player.sendMessage("错误");
                                        }
                                } else {
                                        player.sendMessage("无法识别物品！");
                                }
                                player.sendMessage("成功");
                                player.setItemInHand(NBTReader.readFromBase(base));
                                return true;
                        }
                        player.sendMessage("Bad usage");
                        return true;
                }
                return false;
        }
}