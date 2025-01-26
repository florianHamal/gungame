package at.flori4n.gungame;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Listeners implements Listener {
    private GunGameData gunGameData = GunGameData.getInstance();
    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e) {
        gunGameData.addPlayer(
                new GungamePlayer(e.getPlayer())
        );
        e.getPlayer().teleport(gunGameData.getSpawn());
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
            player.getPlayer().teleport(gunGameData.getSpawn());
        },2);
    }
    @EventHandler
    public void EntityDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player && e.getDamager() instanceof Player))return;
        GungamePlayer player = gunGameData.getGungamePlayer((Player) e.getEntity());
        GungamePlayer damager = gunGameData.getGungamePlayer((Player) e.getDamager());
        if (isInSpawn(player.getPlayer()) || isInSpawn(damager.getPlayer())){
            e.setCancelled(true);
            return;
        }
        player.setLastDamager(damager);
    }
    private boolean isInSpawn(Player e){
        Location spawnLoc = gunGameData.getSpawn();
        Location entityLoc = e.getLocation();
        int radius = gunGameData.getSpawnProtRadius();

        if ((entityLoc.getX()>spawnLoc.getX() - radius) &&
                (entityLoc.getX()<spawnLoc.getX() + radius) &&
                (entityLoc.getZ()>spawnLoc.getZ() - radius) &&
                (entityLoc.getZ()<spawnLoc.getZ() + radius))
        {
            return true;
        }

        return false;
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Material m = e.getPlayer().getLocation().getBlock().getType();
        if (m == Material.WATER||m==Material.STATIONARY_WATER) e.getPlayer().setHealth(0);
    }
}
