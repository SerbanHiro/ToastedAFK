package me.serbob.toastedafk.events.custom.Listener;

import me.serbob.toastedafk.enums.CurrentMove;
import me.serbob.toastedafk.API.events.OnRegionEnteredEvent;
import me.serbob.toastedafk.API.events.OnRegionLeftEvent;
import me.serbob.toastedafk.thread.Tasks;
import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.utils.RegionUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static me.serbob.toastedafk.managers.ValuesManager.*;
import static me.serbob.toastedafk.templates.CoreHelpers.removePlayer;

public class RegionRelatedEventsHandler implements Listener {
    private static final long MOVEMENT_THROTTLE_DELAY = 1000; // 1 second
    private final Map<UUID, Long> lastMovementTimes = new ConcurrentHashMap<>();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (isPlayerInRegion(player.getLocation())) {
            callRegionLeftEvent(player, CurrentMove.DISCONNECT, event);
        } else {
            removePlayerIfInStats(player);
        }
        lastMovementTimes.remove(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        if (shouldThrottleMovement(uuid, currentTime)) {
            return;
        }

        updateRegions(player, CurrentMove.MOVE, event.getTo(), event);
        lastMovementTimes.put(uuid, currentTime);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        updateRegions(event.getPlayer(), CurrentMove.TELEPORT, event.getTo(), event);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updateRegions(event.getPlayer(), CurrentMove.SPAWN, event.getPlayer().getLocation(), event);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        updateRegions(event.getPlayer(), CurrentMove.SPAWN, event.getRespawnLocation(), event);
    }

    private void updateRegions(Player player, CurrentMove movementWay, Location to, PlayerEvent event) {
        CompletableFuture.runAsync(() -> {
            if (!isPlayerInRegion(to)) {
                handlePlayerLeaveRegion(player, movementWay, event);
            } else {
                handlePlayerEnterRegion(player, movementWay, event);
            }
        });
    }

    private boolean isPlayerInRegion(Location location) {
        return RegionUtils.playerInCubiod(location, loc1, loc2);
    }

    private void handlePlayerLeaveRegion(Player player, CurrentMove movementWay, PlayerEvent event) {
        if (playerStats.containsKey(player)) {
            callRegionLeftEvent(player, movementWay, event);
        }
    }

    private void handlePlayerEnterRegion(Player player, CurrentMove movementWay, PlayerEvent event) {
        if (!playerStats.containsKey(player)) {
            callRegionEnteredEvent(player, movementWay, event);
        }
    }

    private void callRegionLeftEvent(Player player, CurrentMove movementWay, PlayerEvent event) {
        OnRegionLeftEvent regionLeftEvent = new OnRegionLeftEvent(player, movementWay, event);
        Tasks.sync(() -> ToastedAFK.instance.getServer().getPluginManager().callEvent(regionLeftEvent));
    }

    private void callRegionEnteredEvent(Player player, CurrentMove movementWay, PlayerEvent event) {
        OnRegionEnteredEvent regionEnteredEvent = new OnRegionEnteredEvent(player, movementWay, event);
        Tasks.sync(() -> ToastedAFK.instance.getServer().getPluginManager().callEvent(regionEnteredEvent));
    }

    private void removePlayerIfInStats(Player player) {
        if (playerStats.containsKey(player)) {
            removePlayer(player);
        }
    }

    private boolean shouldThrottleMovement(UUID uuid, long currentTime) {
        Long lastMovementTime = lastMovementTimes.get(uuid);
        return lastMovementTime != null && currentTime - lastMovementTime < MOVEMENT_THROTTLE_DELAY;
    }
}