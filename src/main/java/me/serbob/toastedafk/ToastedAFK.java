package me.serbob.toastedafk;

import me.serbob.toastedafk.Utils.RegionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ToastedAFK extends JavaPlugin {
    public static ToastedAFK instance;
    private static String REGION_NAME = "AFK";
    private static int TIMEOUT_SECONDS=1200; // 20 minutes in seconds

    private final Map<Player, Integer> afkTimers = new ConcurrentHashMap<>(); // map of player timers

    @Override
    public void onEnable() {
        instance=this;
        saveDefaultConfig();
        TIMEOUT_SECONDS=getConfig().getInt("afk_time");
        REGION_NAME=getConfig().getString("region");
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                for (Player player : getServer().getOnlinePlayers()) {
                    if (RegionUtils.getPlayersInRegion(player.getLocation(), REGION_NAME, ToastedAFK.instance)) {
                        //player.sendMessage("u are in afk region");
                        // check if player is in the region and start or reset their timer
                        if (!afkTimers.containsKey(player)) {
                            afkTimers.put(player, TIMEOUT_SECONDS);
                        }
                    } else {
                        // remove player timer if they leave the region
                        afkTimers.remove(player);
                    }
                }

                // loop through players and execute command if their timer has expired
                for (Map.Entry<Player, Integer> entry : afkTimers.entrySet()) {
                    Player player = entry.getKey();
                    int timeLeft = entry.getValue() - 1;
                    if (timeLeft <= 0) {
                        List<String> list=getConfig().getStringList("commands");
                        for(String key:list) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),key
                                    .replace("{player}",player.getName()));
                        }
                        afkTimers.remove(player);
                    } else {
                        afkTimers.put(player, timeLeft);
                    }
                }
            }
        }, 0L, 20L);
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
