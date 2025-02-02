package at.flori4n.gungame;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;

public class Listeners implements Listener {
    private GunGameData gunGameData = GunGameData.getInstance();
    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e) {
        gunGameData.addPlayer(
                new GungamePlayer(e.getPlayer())
        );
        e.getPlayer().teleport(gunGameData.getRandomSpawn());
    }
    @EventHandler
    public void PlayerQuit(PlayerQuitEvent e) {
        gunGameData.removePlayer(e.getPlayer());
    }
    @EventHandler
    public void PlayerDeath(PlayerDeathEvent e) {
        if (e.getEntity()==null)return;
        if (!(e.getEntity()instanceof Player))return;
        GungamePlayer player = gunGameData.getGungamePlayer(e.getEntity());
        GungamePlayer damager = player.getLastDamager();
        player.setLastDamager(null);
        //setNewLvl
        if (damager!=null){
            damager.lvlUp();
            damager.equip();
        }
        player.lvlDown();
        //auto respawn
        Bukkit.getScheduler().scheduleSyncDelayedTask(GunGame.getPlugin(), () -> {
            player.getPlayer().spigot().respawn();
            player.equip();
            player.getPlayer().teleport(gunGameData.getRandomSpawn());
            System.out.println(gunGameData.getSpawns().size());
        },2);
    }
    @EventHandler
    public void EntityDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player && e.getDamager() instanceof Player))return;
        GungamePlayer player = gunGameData.getGungamePlayer((Player) e.getEntity());
        GungamePlayer damager = gunGameData.getGungamePlayer((Player) e.getDamager());
        player.setLastDamager(damager);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void weatherChangeListener(WeatherChangeEvent e){
        e.setCancelled(true);
        e.getWorld().setThundering(false);
    }
    @EventHandler
    public void blockBreakListener(BlockBreakEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void foodListener(FoodLevelChangeEvent e){
        e.setCancelled(true);
    }
}
