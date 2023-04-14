package me.serbob.toastedafk.Functions;

import me.serbob.toastedafk.Classes.PlayerStats;
import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.List;
import java.util.Set;

import static me.serbob.toastedafk.Managers.ValuesManager.*;

public class AFKCore {
    private static AFKCore instance;
    public static AFKCore getInstance() {
        if(instance==null)
            instance=new AFKCore();
        return instance;
    }
    public void regionCheck() {
        List<String> commands = ToastedAFK.instance.getConfig().getStringList("commands");
        playerStats.forEach((player, playerStatistics) -> {
         //   if(afkTimers.containsKey(player)) {
            int timeLeft = playerStatistics.getAfkTimer();
            if (timeLeft-- <= 0) {
                commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName())));
                // afkTimers.remove(player);
                playerStatistics.setAfkTimer(playerStatistics.getDefaultAfkTime());
            } else {
                //afkTimers.replace(player,timeLeft);
                playerStatistics.setAfkTimer(timeLeft);
            }
        });
    }
    public void addOrRemovePlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (/**RegionUtils.getPlayersInRegion(player.getLocation(),
                    ToastedAFK.instance.getConfig().getString("worldguard_region"),
                    ToastedAFK.instance)*/RegionUtils.playerInCubiod(player.getLocation(),loc1,loc2)) {
                //String rank="";
                TIMEOUT_SECONDS = DEFAULT_AFK_TIME;
                /**if(ToastedAFK.instance.getServer().getPluginManager().getPlugin("LuckPerms")==null) {}
                else if(ToastedAFK.instance.getServer().getPluginManager().getPlugin("LuckPerms").isEnabled()) {
                    rank = net.luckperms.api.LuckPermsProvider.get().getUserManager().getUser(player.getName()).getPrimaryGroup();
                    if (rankTime.get(rank)!=null) {
                        TIMEOUT_SECONDS = rankTime.get(rank);
                    }
                }*/
                if(!playerStats.containsKey(player)) {
                    for (String key : ToastedAFK.instance.getConfig().getConfigurationSection("afk_times").getKeys(false)) {
                        if (player.hasPermission("afk.perm." + key)) {
                            int perm_time = ToastedAFK.instance.getConfig().getInt("afk_times." + key);
                            if (TIMEOUT_SECONDS > perm_time) {
                                TIMEOUT_SECONDS = perm_time;
                            }
                        }
                    }
                    //afkTimers.putIfAbsent(player, TIMEOUT_SECONDS);
                    playerStats.putIfAbsent(player,new PlayerStats(TIMEOUT_SECONDS,TIMEOUT_SECONDS,player.getLevel(),player.getExp(),
                            ToastedAFK.instance.getConfig().getBoolean("save_xp_inside_region")));

                    if(ToastedAFK.instance.getConfig().getBoolean("save_xp_inside_region")||
                    ToastedAFK.instance.getConfig().getBoolean("show_xp_bar")) {
                       /** if(!levelTimer.containsKey(player)) {
                            levelTimer.putIfAbsent(player, player.getLevel());
                        }
                        if(!expTimer.containsKey(player)) {
                            expTimer.putIfAbsent(player, player.getExp());
                        }*/
                        if(ToastedAFK.instance.getConfig().getBoolean("show_xp_bar")) {
                            player.setExp(1.0f);
                        }
                    }
                    if(ToastedAFK.instance.getConfig().getBoolean("bossbar.show")) {
                        bossBar.addPlayer(player);
                    }
                }
                if(playerStats.containsKey(player)) {
                    if(ToastedAFK.instance.getConfig().getBoolean("actionbar.show")) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                               formatActionBar(player)));
                    }
                    if(ToastedAFK.instance.getConfig().getBoolean("bossbar.show")) {
                        if(ToastedAFK.instance.getConfig().getString("bossbar.text").equalsIgnoreCase("{timer}")) {
                            bossBar.setTitle(formatActionBar(player));
                        } else {
                            bossBar.setTitle(AFKUtil.c(ToastedAFK.instance.getConfig().getString("bossbar.text")));
                        }
                    }
                    if(ToastedAFK.instance.getConfig().getBoolean("show_xp_bar")) {
                        player.setLevel(playerStats.get(player).getAfkTimer());
                    }
                }
            } else {
                if(ToastedAFK.instance.getConfig().getBoolean("save_xp_inside_region")||
                        ToastedAFK.instance.getConfig().getBoolean("show_xp_bar")) {
                    //if(ToastedAFK.instance.getConfig().getBoolean("show_xp_bar")) {
                    if(playerStats.containsKey(player)) {
                        player.setLevel(playerStats.get(player).getLevelTimer());
                        player.setExp(playerStats.get(player).getExpTimer());
                    }
                   // }
                  //  levelTimer.remove(player);
                  //  expTimer.remove(player);
                    //playerStats.remove(player);
                }
                if(playerStats.containsKey(player)) {
                    playerStats.remove(player);
                }
                if(ToastedAFK.instance.getConfig().getBoolean("bossbar.show")) {
                    bossBar.removePlayer(player);
                }
            }
        }
    }
    public String formatActionBar(Player player) {
        int time = playerStats.get(player).getAfkTimer();
        int days = time / (60 * 60 * 24);
        int hours = (time % (60 * 60 * 24)) / (60 * 60);
        int minutes = (time % (60 * 60)) / 60;
        int seconds = time % 60;
        String strDays = days==0 ? "" : days==1 ? days%7 +
                AFKUtil.c(ToastedAFK.instance.getConfig().getString("actionbar.messages.sg_day")): days%7 +
                AFKUtil.c(ToastedAFK.instance.getConfig().getString("actionbar.messages.pl_day"));
        String strHours = hours==0 ? "" : hours==1 ? hours +
                AFKUtil.c(ToastedAFK.instance.getConfig().getString("actionbar.messages.sg_hour")): hours+
                AFKUtil.c(ToastedAFK.instance.getConfig().getString("actionbar.messages.pl_hour"));
        String strMinutes = minutes==0 ? "" : minutes==1 ? minutes +
                AFKUtil.c(ToastedAFK.instance.getConfig().getString("actionbar.messages.sg_minute")): minutes +
                AFKUtil.c(ToastedAFK.instance.getConfig().getString("actionbar.messages.pl_minute"));
        String strSeconds = seconds==0 ?
                AFKUtil.c(ToastedAFK.instance.getConfig().getString("actionbar.messages.0_seconds")) : seconds==1 ? seconds +
                AFKUtil.c(ToastedAFK.instance.getConfig().getString("actionbar.messages.sg_second")) : seconds +
                AFKUtil.c(ToastedAFK.instance.getConfig().getString("actionbar.messages.pl_second"));
        String beforeMsg = AFKUtil.c(ToastedAFK.instance.getConfig().getString("actionbar.messages.before_msg")
                .replace("{player}",player.getName()));
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                    beforeMsg + strDays + strHours + strMinutes + strSeconds));
        return beforeMsg + strDays + strHours + strMinutes + strSeconds;
        //try {
          //  player.stopAllSounds();
        //} catch (Exception ignored){}
    }
}
