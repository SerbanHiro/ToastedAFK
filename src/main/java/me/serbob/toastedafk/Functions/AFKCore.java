package me.serbob.toastedafk.Functions;

import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
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
            if (timeLeft-- <= 0) {
                commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName())));
                afkTimers.remove(player);
            } else {
                afkTimers.put(player, timeLeft);
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

                if(afkTimers.get(player)==null) {
                    for (String key : ToastedAFK.instance.getConfig().getConfigurationSection("afk_times").getKeys(false)) {
                        if (player.hasPermission("afk.perm." + key) == true) {
                            int perm_time = ToastedAFK.instance.getConfig().getInt("afk_times." + key);
                            if (TIMEOUT_SECONDS > perm_time) {
                                TIMEOUT_SECONDS = perm_time;
                            }
                        }
                    }
                }

                if(!afkTimers.containsKey(player)) {
                    afkTimers.put(player, TIMEOUT_SECONDS);
                    if(ToastedAFK.instance.getConfig().getBoolean("show_xp_bar")) {
                        if(!levelTimer.containsKey(player)) {
                            levelTimer.put(player, player.getLevel());
                        }
                        if(!expTimer.containsKey(player)) {
                            expTimer.put(player, player.getExp());
                        }
                        player.setExp(1.0f);
                    }
                }
            } else {
                if(ToastedAFK.instance.getConfig().getBoolean("show_xp_bar")) {
                    if (levelTimer.containsKey(player)) {
                        player.setLevel(levelTimer.get(player));
                        player.setExp(expTimer.get(player));
                    }
                    levelTimer.remove(player);
                    expTimer.remove(player);
                }
                afkTimers.remove(player);
            }
        }
    }

}
