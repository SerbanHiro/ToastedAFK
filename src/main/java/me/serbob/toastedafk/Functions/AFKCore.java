package me.serbob.toastedafk.Functions;

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
        afkTimers.forEach((player, timeLeft) -> {
            if(ToastedAFK.instance.getConfig().getBoolean("show_xp_bar")) {
                player.setLevel(afkTimers.get(player));
            }
         //   if(afkTimers.containsKey(player)) {
                if (timeLeft-- <= 0) {
                    commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName())));
                    afkTimers.remove(player);
                } else {
                    afkTimers.replace(player,timeLeft);
                }
          //  } else {
          //     afkTimers.remove(player);
          //  }
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
                if(!afkTimers.containsKey(player)) {
                    for (String key : ToastedAFK.instance.getConfig().getConfigurationSection("afk_times").getKeys(false)) {
                        if (player.hasPermission("afk.perm." + key)) {
                            int perm_time = ToastedAFK.instance.getConfig().getInt("afk_times." + key);
                            if (TIMEOUT_SECONDS > perm_time) {
                                TIMEOUT_SECONDS = perm_time;
                            }
                        }
                    }
                    afkTimers.putIfAbsent(player, TIMEOUT_SECONDS);
                    if(ToastedAFK.instance.getConfig().getBoolean("save_xp_inside_region")) {
                        if(!levelTimer.containsKey(player)) {
                            levelTimer.putIfAbsent(player, player.getLevel());
                        }
                        if(!expTimer.containsKey(player)) {
                            expTimer.putIfAbsent(player, player.getExp());
                        }
                        if(ToastedAFK.instance.getConfig().getBoolean("show_xp_bar")) {
                            player.setExp(1.0f);
                        }
                    }
                }
                if(afkTimers.containsKey(player)) {
                    if(ToastedAFK.instance.getConfig().getBoolean("actionbar.show")) {
                        formatActionBar(player);
                    }
                }
            } else {
                if(ToastedAFK.instance.getConfig().getBoolean("save_xp_inside_region")) {
                    //if(ToastedAFK.instance.getConfig().getBoolean("show_xp_bar")) {
                        if (levelTimer.containsKey(player)) {
                            player.setLevel(levelTimer.get(player));
                            player.setExp(expTimer.get(player));
                        }
                   // }
                    levelTimer.remove(player);
                    expTimer.remove(player);
                }
                afkTimers.remove(player);
            }
        }
    }
    void formatActionBar(Player player) {
        int time = afkTimers.get(player);
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
        player.stopAllSounds();
    }
}
