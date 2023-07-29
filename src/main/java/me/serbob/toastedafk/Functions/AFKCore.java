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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
            timeLeft-=schedulerTimer;
            playerStatistics.setAfkTimer(timeLeft);
        }
    }
    public void updatePlayerStatsSynchronized(Player player) {
        PlayerStats playerStatistics = playerStats.get(player);
        if(playerStatistics.getAfkTimer()==0) {
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
        }
        playerStatistics.setAfkTimer(globalSyncTime);
    }

    public void addOrRemovePlayers() {
        --globalSyncTime;
        playerStats.entrySet().forEach(entry -> {
            Player player = entry.getKey();
            updatePlayer(player);
            if (useRewardSync) {
                updatePlayerStatsSynchronized(player);
            } else {
                updatePlayerStats(player);
            }
        });
        if(globalSyncTime==0)
            globalSyncTime=DEFAULT_AFK_TIME+1;
    }
}