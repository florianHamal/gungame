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
import java.util.Random;

public class GunGameData {
    private static GunGameData instance;
    @Getter
    private List<Kit> kits = new ArrayList<>();
    private List<GungamePlayer> players = new ArrayList<>();
    @Getter
    private List<Location> spawns = new ArrayList<>();
    private Random random = new Random();

    private GunGameData(){
        readConf();
    }

    private void readConf(){
        FileConfiguration config = GunGame.getPlugin().getConfig();
        //if (!config.getBoolean("start"))return;

        //for some reason spring can only read a list of spawns from a section
        if (config.getConfigurationSection("spawn")!=null){
            this.spawns.addAll(
                    (List<Location>) config.getConfigurationSection("spawn").getList("spawns")
            );
        }

        ConfigurationSection kitSection = config.getConfigurationSection("kits");
        if(kitSection==null)return;
        for (String s:kitSection.getKeys(false)){
            ConfigurationSection kitSelection = kitSection.getConfigurationSection(s);
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

        //for some reason spring can only read a list of spawns from a section
        config.createSection("spawn").set("spawns",spawns);
        ConfigurationSection spawnSection = config.createSection("spawns");
        for (int i = 0; i<spawns.size();i++){
            spawnSection.set("spawn"+i,spawns.get(i));
        }


        ConfigurationSection section = config.createSection("kits");
        for (int i = 0; i<kits.size();i++){
            ConfigurationSection kitSection = section.createSection("kit"+i);
            Kit kit = kits.get(i);
            ConfigurationSection itemSection= kitSection.createSection("items");
            for (int j = 0; j<kit.getInvContents().length;j++){
                System.out.println("loopTest");
                if(kit.getInvContents()[j]!=null){
                    itemSection.set("item"+j,kit.getInvContents()[j]);
                }else {
                    itemSection.set("item"+j,"-");
                }
            }
            ConfigurationSection armorSection = kitSection.createSection("armor");
            for (int j = 0; j<kit.getArmorContents().length;j++){
                armorSection.set("armor"+j,kit.getArmorContents()[j]);
            }
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

    public Location getRandomSpawn(){
        return spawns.get(random.nextInt(spawns.size()));
    }

    public static GunGameData getInstance() {
        if (instance == null) instance = new GunGameData();
        return instance;

    }
}
