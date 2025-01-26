package at.flori4n.gungame;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Kit {
    //private String name;
    private ItemStack[] invContents;
    private ItemStack[] armorContents;
/*
    public Kit(String name){
        this.name = name;
    }
*/
    public void applyKit(Player player){
        player.getInventory().setArmorContents(armorContents);
        player.getInventory().setContents(invContents);
        player.updateInventory();
    }

}
