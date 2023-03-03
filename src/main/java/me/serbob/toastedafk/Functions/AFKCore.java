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
            if (--timeLeft <= 0) {
                commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName())));
                afkTimers.remove(player);
            } else {
                afkTimers.put(player, timeLeft);
            }
        });
    }
    public void addOrRemovePlayers() {
        for (Player player : ToastedAFK.instance.getServer().getOnlinePlayers()) {
            if (RegionUtils.getPlayersInRegion(player.getLocation(), REGION_NAME, ToastedAFK.instance)) {
                String rank="";
                if(ToastedAFK.instance.getServer().getPluginManager().getPlugin("LuckPerms").isEnabled()) {
                    rank = net.luckperms.api.LuckPermsProvider.get().getUserManager().getUser(player.getName()).getPrimaryGroup();
                }
                Integer timeoutSeconds = ToastedAFK.instance.getConfig().getInt("afk_times." + rank);
                TIMEOUT_SECONDS = ToastedAFK.instance.getConfig().getInt("default_afk_time");
                if (ToastedAFK.instance.getConfig().getString("afk_times."+rank)!=null) {
                    TIMEOUT_SECONDS = timeoutSeconds;
                }
                afkTimers.putIfAbsent(player, TIMEOUT_SECONDS);
            } else {
                afkTimers.remove(player);
            }
        }
    }
}
