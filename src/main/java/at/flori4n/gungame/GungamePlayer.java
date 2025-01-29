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
        gunGameData.getKits().get(lvl).applyKit(player);
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

    protected int calcXPLevels (int levels) {
        int xp = 0;

        for (int i = 1; i <= levels; i++) {
            if (i <= 16) {
                xp += 17;
            } else if (i > 16 && i <= 31) {
                xp += (i - 16) * 3 + 17;
            } else if (i > 31) {
                xp += (i - 31) * 7 + 62;
            }
        }

        return xp;
    }
}
