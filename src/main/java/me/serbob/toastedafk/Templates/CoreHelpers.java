package me.serbob.toastedafk.Templates;

import me.serbob.toastedafk.API.Events.AFKRewardEvent;
import me.serbob.toastedafk.Classes.PlayerStats;
import me.serbob.toastedafk.Functions.AFKCore;
import me.serbob.toastedafk.Managers.AFKManager;
import me.serbob.toastedafk.NMS.Usages.RefActionbar;
import me.serbob.toastedafk.NMS.Usages.RefTitle;
import me.serbob.toastedafk.Thread.Tasks;
import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.AFKUtil;
import me.serbob.toastedafk.Utils.Logger;
import me.serbob.toastedafk.Utils.RegionUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static me.serbob.toastedafk.Commands.ALLVersionsCommandExecuter.*;
import static me.serbob.toastedafk.Functions.AFKCore.globalSyncTime;
import static me.serbob.toastedafk.Managers.ValuesManager.*;
import static me.serbob.toastedafk.Managers.VersionManager.isVersion1_12OrBelow;
import static me.serbob.toastedafk.Managers.VersionManager.isVersion1_8;
import static me.serbob.toastedafk.Templates.LoadingScreen.*;

public class CoreHelpers {
    public static int schedulerTimer;
    public static boolean saveXpInsideRegion;
    public static boolean showXpBar;
    public static boolean bossBarShow;
    public static boolean actionBarShow;
    public static boolean actionBarShowTimer;
    public static boolean titleScreenShow;
    public static boolean useProbabilityFeature;
    public static boolean useRandomFeature;
    public static boolean useCommands;
    public static String playerEntered;
    public static String playerLeft;
    public static String bossBarText;
    public static boolean useRewardSync;
    public static int timeoutTimes;
    public static void readConfiguration() {
        schedulerTimer = ToastedAFK.instance.getConfig().getInt("how_often_all_players_and_region_checked");
        saveXpInsideRegion = ToastedAFK.instance.getConfig().getBoolean("save_xp_inside_region");
        showXpBar = ToastedAFK.instance.getConfig().getBoolean("show_xp_bar");
        bossBarShow = ToastedAFK.instance.getConfig().getBoolean("bossbar.show");
        actionBarShow = ToastedAFK.instance.getConfig().getBoolean("actionbar.show");
        actionBarShowTimer = ToastedAFK.instance.getConfig().getBoolean("actionbar.show_timer");
        bossBarText = ToastedAFK.instance.getConfig().getString("bossbar.text");
        titleScreenShow = ToastedAFK.instance.getConfig().getBoolean("title_screen.show");
        useProbabilityFeature = ToastedAFK.instance.getConfig().getBoolean("use_probability_feature");
        useRandomFeature = ToastedAFK.instance.getConfig().getBoolean("use_random_feature");
        useCommands = ToastedAFK.instance.getConfig().getBoolean("use_commands");
        playerEntered = ToastedAFK.instance.getConfig().getString("player_entered_region");
        playerLeft = ToastedAFK.instance.getConfig().getString("player_left_region");
        useRewardSync = ToastedAFK.instance.getConfig().getBoolean("reward_synchronization");
        timeoutTimes = ToastedAFK.instance.getConfig().getInt("timeout.times");
    }
    public static void updatePlayer(Player player) {
        //System.out.println(playerStats.get(player).getAfkTimer()+"");
        if(!player.isOnline() || !RegionUtils.playerInCubiod(player.getLocation(),loc1,loc2)) {
            removePlayer(player);
            return;
        }
        if (actionBarShow) {
            sendALLVersionsActionBar(player);
        }
        if (bossBarShow) {
            if (bossBarText.equalsIgnoreCase("{timer}")) {
                bossBar.setTitle(ActionBar.formatNormalActionBar(player));
            } else {
                bossBar.setTitle(AFKUtil.c(bossBarText));
            }
        }
        if (showXpBar) {
            player.setLevel(playerStats.get(player).getAfkTimer());
        }
        if(titleScreenShow) {
            sendALLVersionsTitleScreen(player);
        }
        if (useRewardSync) {
            updatePlayerStatsSynchronized(player);
        } else {
            updatePlayerStats(player);
        }
    }
    public static void addPlayer(Player player) {
        int defaultAfkTime = getDefaultAfkTime(player);
        if(defaultAfkTime==-1) return;
        sendCUSTOMAllVersionsTitleScreen(player,playerEntered);
        boolean playerXpEnabled = saveXpInsideRegion || showXpBar;
        playerStats.putIfAbsent(player, new PlayerStats(useRewardSync ? DEFAULT_AFK_TIME : defaultAfkTime,
                useRewardSync ? globalSyncTime : defaultAfkTime, player.getLevel(),
                player.getExp(), playerXpEnabled,timeoutTimes));
        if (showXpBar) {
            player.setExp(1.0f);
        }
        if (bossBarShow) {
            bossBar.addPlayer(player);
        }
    }
    public static void removePlayer(Player player) {
        //AFKCore.getInstance().removePlayerFromCache(player); <-- Used for caching, not using it yet
        sendCUSTOMAllVersionsTitleScreen(player,playerLeft);
        if (saveXpInsideRegion || showXpBar) {
            player.setLevel(playerStats.get(player).getLevelTimer());
            player.setExp(playerStats.get(player).getExpTimer());
        }
        if (playerStats.containsKey(player)) {
            playerStats.remove(player);
        }
        if (bossBarShow) {
            bossBar.removePlayer(player);
        }
    }
    private static int getDefaultAfkTime(Player player) {
        int defaultAfkTime = DEFAULT_AFK_TIME;
        for (String key : ToastedAFK.instance.getConfig().getConfigurationSection("afk_times").getKeys(false)) {
            if (player.hasPermission("afk.perm." + key)) {
                int perm_time = ToastedAFK.instance.getConfig().getInt("afk_times." + key);
                if (defaultAfkTime > perm_time || defaultAfkTime == -1) {
                    defaultAfkTime = perm_time;
                }
            }
        }
        return defaultAfkTime;
    }
    public static void executeCommands(Player player, List<String> commands) {
        Tasks.sync(() -> {
            for(String command:commands) {
                command = command.replace("{player}",player.getName());
                try{Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);}
                catch (Exception ignored){
                    Logger.log(Logger.LogLevel.ERROR,AFKUtil.c("&cThe command \"&f"+command+"&c\" doesn't work!"));
                }
            }
        });
    }
    public static void executeRandomCommands(Player player) {
        Tasks.sync(()-> {
            List<String> commands = new ArrayList<>(ToastedAFK.instance.getConfig().getConfigurationSection("random_commands.list").getKeys(false));
            int timesToExecute = ToastedAFK.instance.getConfig().getInt("random_commands.times");
            for (int i = 0; i < timesToExecute; i++) {
                Collections.shuffle(commands);
                if (!commands.isEmpty()) {
                    int randomIndex = ThreadLocalRandom.current().nextInt(commands.size());
                    String listToBeExec = commands.get(randomIndex);
                    for(String command:ToastedAFK.instance.getConfig().getStringList("random_commands.list."+listToBeExec)) {
                        command = command.replace("{player}", player.getName());
                        try {
                            ToastedAFK.instance.getServer().dispatchCommand(ToastedAFK.instance.getServer().getConsoleSender(), command);
                        } catch (Exception ignored) {
                            Logger.log(Logger.LogLevel.ERROR, AFKUtil.c("&cThe command \"&f" + command + "&c\" doesn't work!"));
                        }
                    }
                }
            }
        });
    }
    public static void serverCrash() {
        Iterator<Map.Entry<Player, PlayerStats>> iterator = playerStats.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Player, PlayerStats> entry = iterator.next();
            Player player = entry.getKey();
            PlayerStats playerStatistic = entry.getValue();

            // Use playerStats to set the player's level and other stats
            int level = playerStatistic.getLevelTimer();
            float exp = playerStatistic.getExpTimer();
            player.setLevel(level);
            player.setExp(exp);

            // Remove the player's stats from the map
            iterator.remove();
        }
        if(bossBarShow) {
            bossBar.removeAll();
        }
    }
    public static void updatePlayerStats(Player player) {
        PlayerStats playerStatistics = playerStats.get(player);

        int timeLeft = playerStatistics.getAfkTimer();
        if (timeLeft <= 0) {
            playerStatistics.setAfkTimer(playerStatistics.getDefaultAfkTime());
            if (useProbabilityFeature) {
                ItemDistribution.distributeCommands(player);
            }
            if (useCommands) {
                CoreHelpers.executeCommands(player,ToastedAFK.instance.getConfig().getStringList("commands"));
            }
            if (useRandomFeature) {
                CoreHelpers.executeRandomCommands(player);
            }

            Tasks.sync(()-> {
                AFKRewardEvent afkRewardEvent = new AFKRewardEvent(player,playerStatistics);
                ToastedAFK.instance.getServer().getPluginManager().callEvent(afkRewardEvent);
            });

            if(timeoutTimes>0) {
                if(playerStatistics.getTimeoutTimes()>=timeoutTimes) {
                    CoreHelpers.executeCommands(player,ToastedAFK.instance.getConfig().getStringList("timeout.commands"));
                    removePlayer(player);
                }
            }
        } else {
            timeLeft-=schedulerTimer;
            playerStatistics.setAfkTimer(timeLeft);
        }
    }
    public static void updatePlayerStatsSynchronized(Player player) {
        PlayerStats playerStatistics = playerStats.get(player);
        int timeLeft = playerStatistics.getAfkTimer();
        if(timeLeft<=0) {
            playerStatistics.setAfkTimer(playerStatistics.getDefaultAfkTime());
            if (useProbabilityFeature) {
                ItemDistribution.distributeCommands(player);
            }
            if (useCommands) {
                CoreHelpers.executeCommands(player,ToastedAFK.instance.getConfig().getStringList("commands"));
            }
            if (useRandomFeature) {
                CoreHelpers.executeRandomCommands(player);
            }

            Tasks.sync(() -> {
                AFKRewardEvent afkRewardEvent = new AFKRewardEvent(player,playerStatistics);
                ToastedAFK.instance.getServer().getPluginManager().callEvent(afkRewardEvent);
            });

            if(timeoutTimes>0) {
                if(playerStatistics.getTimeoutTimes()>=timeoutTimes) {
                    CoreHelpers.executeCommands(player,ToastedAFK.instance.getConfig().getStringList("timeout.commands"));
                    removePlayer(player);
                }
            }
        } else {
            playerStatistics.setAfkTimer(globalSyncTime);
        }
    }
}
