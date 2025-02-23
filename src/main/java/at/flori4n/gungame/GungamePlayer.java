package at.flori4n.gungame;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.awt.*;

public class GungamePlayer {
    GunGameData gunGameData = GunGameData.getInstance();
    @Getter
    @Setter
    private Player player;
    @Getter
    @Setter
    private GungamePlayer lastDamager;
    @Getter
    private int lvl = 1;

    public GungamePlayer(Player player) {
        this.player = player;
        equip();
    }
    public void equip(){
        //-1 cuz indexing starts at 0
        gunGameData.getKits().get(lvl-1).applyKit(player);
        player.sendMessage("Du bist jetzt Lvl "+ChatColor.GOLD+lvl);
        //player.setExp(calcXPLevels(lvl));
        player.setLevel(lvl);
        Bukkit.getPluginManager().callEvent(new PlayerPickupItemEvent(player.getPlayer(), null, Event.ACTION_EVENT));
    }
    public void lvlUp(){
        if (gunGameData.getKits().size()<lvl+1){
            GunGame.getPlugin().endGame(this);
            return;
        }
        lvl++;
        double health = player.getHealth();
            health+=6;
            if (health>20) {
                health=20;
            }
        player.setHealth(health);
        equip();
        gunGameData.updateAllScoreboards();
    }

    public void lvlDown(){
        if (lvl>1)lvl--;
        equip();
        gunGameData.updateAllScoreboards();
    }
}
