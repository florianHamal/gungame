package at.flori4n.gungame;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.github.paperspigot.Title;

public final class GunGame extends JavaPlugin {

    private static GunGame instance;
    private Listeners listeners;
    @Override
    public void onEnable() {
        instance = this;
        getCommand("gungame").setExecutor(new Commands());
        getConfig().addDefault("start","false");
        saveConfig();
        if (this.getConfig().getBoolean("start")){
            listeners= new Listeners();
            Bukkit.getPluginManager().registerEvents(listeners,this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public static GunGame getPlugin(){
        return instance;
    }
    public void endGame(GungamePlayer winner){

        HandlerList.unregisterAll(listeners);
        Bukkit.getPluginManager().registerEvents(new GameOverListener(),this);

        for(Player player : Bukkit.getOnlinePlayers()){
            player.sendTitle(new Title(ChatColor.GOLD+winner.getPlayer().getDisplayName(),"hat gewonnen"));
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(instance,new Runnable(){
            @Override
            public void run() {
                Bukkit.shutdown();
            }
        },100);
    }
}
