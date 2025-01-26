package at.flori4n.gungame;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class GunGame extends JavaPlugin {

    private static GunGame instance;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(new Listeners(),this);
        getCommand("gungame").setExecutor(new Commands());
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public static GunGame getPlugin(){
        return instance;
    }
}
