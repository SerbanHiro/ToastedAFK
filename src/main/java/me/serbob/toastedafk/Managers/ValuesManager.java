package me.serbob.toastedafk.Managers;

import me.serbob.toastedafk.ToastedAFK;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ValuesManager {
    public static Map<Player, Integer> afkTimers = new ConcurrentHashMap<>(); // map of player timers
    public static String REGION_NAME = "AFK";
    public static int TIMEOUT_SECONDS=1200; // 20 minutes in seconds
    public static Location loc1;
    public static Location loc2;
    public static Location tempLoc1;
    public static Location tempLoc2;
    public static void loadConfigValues() {
        REGION_NAME=ToastedAFK.instance.getConfig().getString("region");
        loc1 = new Location(Bukkit.getWorld(ToastedAFK.instance.getConfig().getString("region.locations.world")),
                ToastedAFK.instance.getConfig().getDouble("region.locations.loc1.x"),
                ToastedAFK.instance.getConfig().getDouble("region.locations.loc1.y"),
                ToastedAFK.instance.getConfig().getDouble("region.locations.loc1.z"));
        loc2 = new Location(Bukkit.getWorld(ToastedAFK.instance.getConfig().getString("region.locations.world")),
                ToastedAFK.instance.getConfig().getDouble("region.locations.loc2.x"),
                ToastedAFK.instance.getConfig().getDouble("region.locations.loc2.y"),
                ToastedAFK.instance.getConfig().getDouble("region.locations.loc2.z"));
    }
}
