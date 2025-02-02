package at.flori4n.gungame;

import net.md_5.bungee.chat.BaseComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        GunGameData gunGameData = GunGameData.getInstance();
        Player player = (Player) commandSender;
        if (!player.hasPermission("setup"))return false;
        Kit kit = new Kit();
        try {
            switch (strings[0]) {
                case "addKitAtIndex":
                    int index = Integer.parseInt(strings[1]);
                    kit.setInvContents(player.getInventory().getContents());
                    kit.setArmorContents(player.getInventory().getArmorContents());
                    gunGameData.getKits().add(index,kit);
                    break;
                case "addKit":
                    kit.setInvContents(player.getInventory().getContents());
                    kit.setArmorContents(player.getInventory().getArmorContents());
                    gunGameData.addKit(kit);
                    break;
                case "save":
                    gunGameData.writeConf();
                    break;
                case "select":
                    kit = gunGameData.getKit(Integer.parseInt(strings[1]));
                    player.getInventory().setArmorContents(kit.getArmorContents());
                    player.getInventory().setContents(kit.getInvContents());
                    player.updateInventory();
                    break;
                case "printLvlAmount":
                    player.sendMessage(gunGameData.getKits().size()+" kits");
                    break;
                case "removeKit":
                    gunGameData.removeKit(Integer.parseInt(strings[1]));
                    break;
                case "updateKit":
                    kit = gunGameData.getKit(Integer.parseInt(strings[1]));
                    kit.setInvContents(player.getInventory().getContents());
                    kit.setArmorContents(player.getInventory().getArmorContents());
                    break;
                case "addSpawn":
                    gunGameData.getSpawns().add(player.getLocation());
                    break;
                case "setStart":
                    GunGame.getPlugin().getConfig().set("start",Boolean.valueOf(strings[1]));
                    //GunGame.getPlugin().saveConfig();
                    break;
                default:
                    player.sendMessage("wrong Command");
            }
        }catch (RuntimeException e){
            player.sendMessage(e.getMessage());
        }
        return false;
    }
}
