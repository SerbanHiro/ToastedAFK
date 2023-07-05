package me.serbob.toastedafk.Events.Custom.Listener;

import me.serbob.toastedafk.Enums.CurrentMove;
import me.serbob.toastedafk.Events.Custom.OnRegionEnteredEvent;
import me.serbob.toastedafk.Events.Custom.OnRegionLeftEvent;
import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.RegionUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static me.serbob.toastedafk.Managers.ValuesManager.*;

public class RegionRelatedEventsHandler implements Listener {
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        if(RegionUtils.playerInCubiod(event.getPlayer().getLocation(),loc1,loc2)) {
            OnRegionLeftEvent leaveEvent = new OnRegionLeftEvent(event.getPlayer(), CurrentMove.DISCONNECT, event);
            ToastedAFK.instance.getServer().getPluginManager().callEvent(leaveEvent);
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(RegionUtils.playerInCubiod(event.getPlayer().getLocation(),loc1,loc2)) {
            OnRegionLeftEvent leaveEvent = new OnRegionLeftEvent(event.getPlayer(), CurrentMove.DISCONNECT, event);
            ToastedAFK.instance.getServer().getPluginManager().callEvent(leaveEvent);
        }
    }
    /**@EventHandler <-- might use this but for the moment using concurrenthashmaps seems to be more efficient
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        long currentTime = System.currentTimeMillis();
        List<MetadataValue> metadataValues = player.getMetadata("lastMovementTime");
        if (!metadataValues.isEmpty()) {
            long lastMovementTime = ((MetadataValue)metadataValues.get(0)).asLong();
            if (currentTime - lastMovementTime < 1000L)
                return;
        }
        event.setCancelled(updateRegions(player, CurrentMove.MOVE, event.getTo(), (PlayerEvent)event));
        player.setMetadata("lastMovementTime", (MetadataValue)new FixedMetadataValue((Plugin)ToastedAFK.instance, Long.valueOf(currentTime)));
    }*/
    private final ConcurrentHashMap<Player, Long> lastMovementTimes = new ConcurrentHashMap<>();
    private static final long MOVEMENT_THROTTLE_DELAY = 1000; // 1 second
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        long currentTime = System.currentTimeMillis();

        if (lastMovementTimes.containsKey(player)) {
            long lastMovementTime = lastMovementTimes.get(player);
            if (currentTime - lastMovementTime < MOVEMENT_THROTTLE_DELAY) {
                return; // Skip event handling if the movement occurred too soon
            }
        }
        // Process the movement event
        event.setCancelled(updateRegions(player, CurrentMove.MOVE, event.getTo(), event));
        // Update the last movement time for the player
        lastMovementTimes.put(player, currentTime);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        event.setCancelled(updateRegions(event.getPlayer(), CurrentMove.TELEPORT,event.getTo(),event));
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updateRegions(event.getPlayer(), CurrentMove.SPAWN,event.getPlayer().getLocation(),event);
    }
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        updateRegions(event.getPlayer(), CurrentMove.SPAWN,event.getRespawnLocation(),event);
    }
    private boolean updateRegions(Player player, CurrentMove movementWay, Location to, PlayerEvent event) {
        if(!RegionUtils.playerInCubiod(to,loc1,loc2)) {
            if(playerStats.containsKey(player)) {
                OnRegionLeftEvent regionLeftEvent = new OnRegionLeftEvent(player,movementWay,event);
                ToastedAFK.instance.getServer().getPluginManager().callEvent(regionLeftEvent);
            }
        } else {
            if(!playerStats.containsKey(player)) {
                OnRegionEnteredEvent regionEnteredEvent = new OnRegionEnteredEvent(player, movementWay, event);
                ToastedAFK.instance.getServer().getPluginManager().callEvent(regionEnteredEvent);
            }
        }
        return false;
    }
}
