package me.serbob.toastedafk.Functions;

import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

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
            if (RegionUtils.playerInCubiod(player.getLocation(), loc1,loc2)) {
                String rank="";
                TIMEOUT_SECONDS = DEFAULT_AFK_TIME;
                if(ToastedAFK.instance.getServer().getPluginManager().getPlugin("LuckPerms")==null) {}
                else if(ToastedAFK.instance.getServer().getPluginManager().getPlugin("LuckPerms").isEnabled()) {
                    rank = net.luckperms.api.LuckPermsProvider.get().getUserManager().getUser(player.getName()).getPrimaryGroup();
                    if (rankTime.get(rank)!=null) {
                        TIMEOUT_SECONDS = rankTime.get(rank);
                    }
                }
                if(!afkTimers.containsKey(player)) {
                    afkTimers.put(player, TIMEOUT_SECONDS);
                    if(ToastedAFK.instance.getConfig().getBoolean("show_xp_bar")) {
                        levelTimer.put(player, player.getLevel());
                        expTimer.put(player, player.getExp());
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
