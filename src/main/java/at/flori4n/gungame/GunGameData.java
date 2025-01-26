package at.flori4n.gungame;


import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GunGameData {
    private static GunGameData instance;
    @Getter
    private List<Kit> kits = new ArrayList<>();
    private List<GungamePlayer> players = new ArrayList<>();
    @Getter
    @Setter
    private Location spawn;
    @Getter
    @Setter
    private int spawnProtRadius;

    private GunGameData(){
        readConf();
    }

    private void readConf(){
        FileConfiguration config = GunGame.getPlugin().getConfig();
        spawn = (Location) config.get("spawn");
        spawnProtRadius = config.getInt("spawnProtRadius");
        ConfigurationSection section = config.getConfigurationSection("kits");
        if(section==null)return;
        for (String s:section.getKeys(false)){
            ConfigurationSection kitSelection = section.getConfigurationSection(s);
            Kit kit = new Kit();
            ConfigurationSection itemSelection = kitSelection.getConfigurationSection("items");
            ItemStack[] items = new ItemStack[itemSelection.getKeys(false).size()];
            System.out.println(itemSelection.getKeys(false).size());
            for (int i = 0; i< items.length;i++){
                items[i] = itemSelection.getItemStack("item"+i);
            }
            kit.setInvContents(items);
            ConfigurationSection armorSection = kitSelection.getConfigurationSection("armor");
            ItemStack[] armor = new ItemStack[armorSection.getKeys(false).size()];
            for (int i = 0; i< armor.length;i++){
                armor[i] = armorSection.getItemStack("armor"+i);
            }
            kit.setArmorContents(armor);
            kits.add(kit);
        }
    }
    public void writeConf(){
        FileConfiguration config = GunGame.getPlugin().getConfig();
        ConfigurationSection section = config.createSection("kits");
        config.set("spawn",spawn);
        config.set("spawnProtRadius",spawnProtRadius);
        int index = 0;
        for (Kit kit : kits){
            ConfigurationSection kitSection = section.createSection(String.valueOf(index));
            System.out.println(kit.getInvContents().length);
            ConfigurationSection itemSection= kitSection.createSection("items");
            for (int i = 0; i<kit.getInvContents().length;i++){
                System.out.println("loopTest");
                if(kit.getInvContents()[i]!=null){
                    itemSection.set("item"+i,kit.getInvContents()[i]);
                }else {
                    itemSection.set("item"+i,"-");
                }
            }
            ConfigurationSection armorSection = kitSection.createSection("armor");
            for (int i = 0; i<kit.getArmorContents().length;i++){
                armorSection.set("armor"+i,kit.getArmorContents()[i]);
            }
            index++;
        }
        GunGame.getPlugin().saveConfig();
    }
    public void addKit(Kit kit){
        kits.add(kit);
    }
    public Kit getKit(int index){
        return kits.get(index);
    }
    public void removeKit(int index){
        kits.remove(index);
    }
    public void addPlayer(GungamePlayer player){
        players.add(player);
    }
    public void removePlayer(GungamePlayer player){
        players.remove(player);
    }
    public void removePlayer(Player player){
        players.remove(
                players.stream()
                        .filter(p-> Objects.equals(p.getPlayer().getName(), player.getName()))
                        .findFirst()
                        .get()
        );
    }
    public GungamePlayer getGungamePlayer(Player player){
        return players.stream()
                .filter(p-> Objects.equals(p.getPlayer().getName(), player.getName()))
                .findFirst()
                .get();
    }


    private void initKits(){
        kits = new ArrayList<>();

    }


    public static GunGameData getInstance() {
        if (instance == null) instance = new GunGameData();
        return instance;

    }
}
