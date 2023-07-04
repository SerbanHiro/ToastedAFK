package me.serbob.toastedafk.Functions;

import me.serbob.toastedafk.Classes.PlayerStats;
import me.serbob.toastedafk.Managers.ValuesManager;
import me.serbob.toastedafk.Templates.ActionBar;
import me.serbob.toastedafk.Templates.CoreHelpers;
import me.serbob.toastedafk.Templates.ItemDistribution;
import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static me.serbob.toastedafk.Commands.ALLVersionsCommandExecuter.sendCUSTOMAllVersionsTitleScreen;
import static me.serbob.toastedafk.Managers.ValuesManager.*;
import static me.serbob.toastedafk.Templates.CoreHelpers.*;
public class AFKCore {
    public static int globalSyncTime = DEFAULT_AFK_TIME+1;
    private static AFKCore instance;
    public static AFKCore getInstance() {
        if (instance == null)
            instance = new AFKCore();
        return instance;
    }
    public void updatePlayerStats(Player player) {
        PlayerStats playerStatistics = playerStats.get(player);

        int timeLeft = playerStatistics.getAfkTimer();
        if (timeLeft <= 0) {
            if (isPlayerInRegion(player,loc1,loc2)) {
                playerStatistics.setAfkTimer(playerStatistics.getDefaultAfkTime());
                if (useProbabilityFeature) {
                    ItemDistribution.distributeCommands(player);
                }
                if (useCommands) {
                    CoreHelpers.executeCommands(player);
                }
                if (useRandomFeature) {
                    CoreHelpers.executeRandomCommands(player);
                }
            } else {
                removePlayer(player);
            }
        } else {
            timeLeft-=schedulerTimer;
            playerStatistics.setAfkTimer(timeLeft);
        }
    }
    public void updatePlayerStatsSynchronized(Player player) {
        PlayerStats playerStatistics = playerStats.get(player);
        if(playerStatistics.getAfkTimer()==0) {
            if (isPlayerInRegion(player,loc1,loc2)) {
                playerStatistics.setAfkTimer(playerStatistics.getDefaultAfkTime());
                if (useProbabilityFeature) {
                    ItemDistribution.distributeCommands(player);
                }
                if (useCommands) {
                    CoreHelpers.executeCommands(player);
                }
                if (useRandomFeature) {
                    CoreHelpers.executeRandomCommands(player);
                }
            } else {
                removePlayer(player);
            }
        }
        playerStatistics.setAfkTimer(globalSyncTime);
    }
    //private Spliterator<Player> spliterator = null;

    public void addOrRemovePlayers() {
        /**Iterable<Player> players = playerTree.find(bounds);
        for (Player player : players) {
            if (!playerStats.containsKey(player))
                addPlayer(player);
            updatePlayer(player);
            updatePlayerStats(player);
        }*/
        --globalSyncTime;
        for (Chunk chunk : ValuesManager.chunksInRegion) {
            for (Entity entity : chunk.getEntities()) {
                if (entity instanceof Player) {
                    Player player = (Player)entity;
                    if (RegionUtils.playerInCubiod(player.getLocation(), loc1, loc2)) {
                        if (!ValuesManager.playerStats.containsKey(player)) {
                            CoreHelpers.addPlayer(player);
                        } else {
                            updatePlayer(player);
                            if(useRewardSync) {
                                updatePlayerStatsSynchronized(player);
                            }
                            else updatePlayerStats(player);
                        }
                    } else if (ValuesManager.playerStats.containsKey(player)) {
                        removePlayer(player);
                    }
                }
            }
        }
        if(globalSyncTime==0)
            globalSyncTime=DEFAULT_AFK_TIME+1;
    }

    // Still experimental caching algorithm, not using it yet for public
    public static Map<Player, Boolean> playerRegionCache = new HashMap<>();
    private boolean isPlayerInRegion(Player player, Location loc1, Location loc2) {
        Boolean cachedResult = playerRegionCache.get(player);
        if (cachedResult != null) {
            return true;
        }

        boolean result = RegionUtils.playerInCubiod(player.getLocation(), loc1, loc2);
        if(result)
            playerRegionCache.put(player, true);
        return result;
    }
    public void removePlayerFromCache(Player player) {
        playerRegionCache.remove(player);
    }
    /**public void addOrRemovePlayers() {
     for (Player player : Bukkit.getOnlinePlayers()) {
     if (RegionUtils.playerInCubiod(player.getLocation(), loc1, loc2)) {
     if(!playerStats.containsKey(player)) addPlayer(player);
     updatePlayer(player);
     } else {
     if(playerStats.containsKey(player)) removePlayer(player);
     }
     }
     }*/
    /**public static class PlayerMBRConverter implements MBRConverter<Player> {
        @Override
        public int getDimensions() {
            return 3; // Assuming x, y, z coordinates
        }

        @Override
        public double getMin(int axis, Player player) {
            Location location = player.getLocation();
            switch (axis) {
                case 0:
                    return location.getX();
                case 1:
                    return location.getY();
                case 2:
                    return location.getZ();
                default:
                    throw new IllegalArgumentException("Invalid axis: " + axis);
            }
        }

        @Override
        public double getMax(int axis, Player player) {
            Location location = player.getLocation();
            switch (axis) {
                case 0:
                    return location.getX();
                case 1:
                    return location.getY();
                case 2:
                    return location.getZ();
                default:
                    throw new IllegalArgumentException("Invalid axis: " + axis);
            }
        }
    }*/
}