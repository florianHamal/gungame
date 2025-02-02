package at.flori4n.gungame;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
    }
    public void lvlUp(){
        if (gunGameData.getKits().size()<lvl+1)
            GunGame.getPlugin().endGame(this);
        else
            lvl++;
    }
    public void lvlDown(){
        if (lvl>1)lvl--;
    }
}
