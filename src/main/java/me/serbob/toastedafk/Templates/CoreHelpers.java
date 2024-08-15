package me.serbob.toastedafk.Templates;

import me.serbob.toastedafk.API.Events.AFKRewardEvent;
import me.serbob.toastedafk.Classes.PlayerStats;
import me.serbob.toastedafk.Thread.Tasks;
import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.ChatUtil;
import me.serbob.toastedafk.Utils.Logger;
import me.serbob.toastedafk.Utils.RegionUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static me.serbob.toastedafk.Commands.ALLVersionsCommandExecuter.*;
import static me.serbob.toastedafk.Functions.AFKCore.globalSyncTime;
import static me.serbob.toastedafk.Managers.ValuesManager.*;

public class CoreHelpers {
    public static int
            schedulerTimer;
    public static boolean
            saveXpInsideRegion,
            showXpBar,
            bossBarShow,
            actionBarShow,
            actionBarShowTimer,
            titleScreenShow;
    public static boolean
            useProbabilityFeature,
            useRandomFeature,
            useCommands,
            useRewardSync;
    public static String
            playerEntered,
            playerLeft,
            bossBarText;
    public static int
            timeoutTimes;

    public static void readConfiguration() {
        ConfigurationSection config = ToastedAFK.instance.getConfig();
        schedulerTimer = config.getInt("how_often_all_players_and_region_checked");
        saveXpInsideRegion = config.getBoolean("save_xp_inside_region");
        showXpBar = config.getBoolean("show_xp_bar");
        bossBarShow = config.getBoolean("bossbar.show");
        actionBarShow = config.getBoolean("actionbar.show");
        actionBarShowTimer = config.getBoolean("actionbar.show_timer");
        bossBarText = config.getString("bossbar.text");
        titleScreenShow = config.getBoolean("title_screen.show");
        useProbabilityFeature = config.getBoolean("use_probability_feature");
        useRandomFeature = config.getBoolean("use_random_feature");
        useCommands = config.getBoolean("use_commands");
        playerEntered = config.getString("player_entered_region");
        playerLeft = config.getString("player_left_region");
        useRewardSync = config.getBoolean("reward_synchronization");
        timeoutTimes = config.getInt("timeout.times");
    }

    public static void updatePlayer(Player player) {
        if(!player.isOnline() || !RegionUtils.playerInCubiod(player.getLocation(), loc1, loc2)) {
            removePlayer(player);
            return;
        }

        PlayerStats stats = playerStats.get(player);
        if (stats == null) return;

        if (actionBarShow) sendALLVersionsActionBar(player);
        if (bossBarShow) updateBossBar(player);
        if (showXpBar) player.setLevel(stats.getAfkTimer());
        if (titleScreenShow) sendALLVersionsTitleScreen(player);

        if (useRewardSync) updatePlayerStatsSynchronized(player);
        else updatePlayerStats(player);
    }

    public static void addPlayer(Player player) {
        int defaultAfkTime = getDefaultAfkTime(player);
        if(defaultAfkTime == -1) return;

        sendCUSTOMAllVersionsTitleScreen(player, playerEntered);
        boolean playerXpEnabled = saveXpInsideRegion || showXpBar;
        playerStats.putIfAbsent(player, new PlayerStats(useRewardSync ? DEFAULT_AFK_TIME : defaultAfkTime,
                useRewardSync ? globalSyncTime : defaultAfkTime, player.getLevel(),
                player.getExp(), playerXpEnabled, timeoutTimes));

        if (showXpBar) player.setExp(1.0f);
        if (bossBarShow) bossBar.addPlayer(player);
    }

    public static void removePlayer(Player player) {
        sendCUSTOMAllVersionsTitleScreen(player, playerLeft);
        PlayerStats stats = playerStats.remove(player);
        if (stats != null && (saveXpInsideRegion || showXpBar)) {
            player.setLevel(stats.getLevelTimer());
            player.setExp(stats.getExpTimer());
        }
        if (bossBarShow) bossBar.removePlayer(player);
    }

    private static int getDefaultAfkTime(Player player) {
        ConfigurationSection afkTimes = ToastedAFK.instance.getConfig().getConfigurationSection("afk_times");
        if (afkTimes == null) return DEFAULT_AFK_TIME;

        return afkTimes.getKeys(false).stream()
                .filter(key -> player.hasPermission("afk.perm." + key))
                .mapToInt(key -> afkTimes.getInt(key))
                .min()
                .orElse(DEFAULT_AFK_TIME);
    }

    public static void executeCommands(Player player, List<String> commands) {
        Tasks.sync(() -> {
            for(String command : commands) {
                try {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName()));
                } catch (Exception e) {
                    Logger.log(Logger.LogLevel.ERROR, ChatUtil.c("&cThe command \"&f" + command + "&c\" doesn't work!"));
                }
            }
        });
    }

    public static void executeRandomCommands(Player player) {
        Tasks.sync(() -> {
            ConfigurationSection randomCommands = ToastedAFK.instance.getConfig().getConfigurationSection("random_commands");
            if (randomCommands == null) return;

            List<String> commandSets = new ArrayList<>(randomCommands.getConfigurationSection("list").getKeys(false));
            int timesToExecute = randomCommands.getInt("times");

            for (int i = 0; i < timesToExecute && !commandSets.isEmpty(); i++) {
                String listToBeExec = commandSets.get(ThreadLocalRandom.current().nextInt(commandSets.size()));
                for (String command : randomCommands.getStringList("list." + listToBeExec)) {
                    try {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName()));
                    } catch (Exception e) {
                        Logger.log(Logger.LogLevel.ERROR, ChatUtil.c("&cThe command \"&f" + command + "&c\" doesn't work!"));
                    }
                }
            }
        });
    }

    public static void serverCrash() {
        playerStats.forEach((player, stats) -> {
            player.setLevel(stats.getLevelTimer());
            player.setExp(stats.getExpTimer());
        });
        playerStats.clear();
        if(bossBarShow) bossBar.removeAll();
    }

    public static void updatePlayerStats(Player player) {
        PlayerStats stats = playerStats.get(player);
        if (stats == null) return;

        int timeLeft = stats.getAfkTimer() - schedulerTimer;
        if (timeLeft <= 0) {
            rewardPlayer(player, stats);
        } else {
            stats.setAfkTimer(timeLeft);
        }
    }

    public static void updatePlayerStatsSynchronized(Player player) {
        PlayerStats stats = playerStats.get(player);
        if (stats == null) return;

        if (stats.getAfkTimer() <= 0) {
            rewardPlayer(player, stats);
        } else {
            stats.setAfkTimer(globalSyncTime);
        }
    }

    private static void rewardPlayer(Player player, PlayerStats stats) {
        stats.setAfkTimer(stats.getDefaultAfkTime());
        if (useProbabilityFeature) ItemDistribution.distributeCommands(player);
        if (useCommands) executeCommands(player, ToastedAFK.instance.getConfig().getStringList("commands"));
        if (useRandomFeature) executeRandomCommands(player);

        Tasks.sync(() -> {
            AFKRewardEvent afkRewardEvent = new AFKRewardEvent(player, stats);
            ToastedAFK.instance.getServer().getPluginManager().callEvent(afkRewardEvent);
        });

        if (timeoutTimes > 0 && stats.getTimeoutTimes() >= timeoutTimes) {
            executeCommands(player, ToastedAFK.instance.getConfig().getStringList("timeout.commands"));
            removePlayer(player);
        }
    }

    private static void updateBossBar(Player player) {
        bossBar.setTitle(bossBarText.equalsIgnoreCase("{timer}")
                ? ActionBar.formatNormalActionBar(player)
                : ChatUtil.c(bossBarText));
    }
}