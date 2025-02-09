package at.flori4n.gungame;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.awt.*;

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
        GungamePlayer player = gunGameData.getGungamePlayer(e.getEntity());
        GungamePlayer damager = player.getLastDamager();
        //setNewLvl
        if (damager!=null){
            damager.lvlUp();
            damager.equip();
        }
        player.setLastDamager(null);
        player.lvlDown();
        //auto respawn
        Bukkit.getScheduler().scheduleSyncDelayedTask(GunGame.getPlugin(), () -> {
            player.getPlayer().spigot().respawn();
            player.equip();
            player.getPlayer().teleport(gunGameData.getRandomSpawn());
        },1);
    }
    @EventHandler
    public void EntityDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player))return;
        GungamePlayer player = gunGameData.getGungamePlayer((Player) e.getEntity());
        if (e.getDamager() instanceof Projectile){
            Projectile p = (Projectile) e.getDamager();
            if (p.getShooter() instanceof Player){
                GungamePlayer damager = gunGameData.getGungamePlayer((Player) p.getShooter());
                player.setLastDamager(damager);
            }
        }else if (e.getDamager() instanceof Player){
            GungamePlayer damager = gunGameData.getGungamePlayer((Player) e.getDamager());
            player.setLastDamager(damager);
        }

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
        if (e.getPlayer().hasPermission("*"))return;
        e.setCancelled(true);
    }
    @EventHandler
    public void foodListener(FoodLevelChangeEvent e){
        e.setCancelled(true);
    }


}
