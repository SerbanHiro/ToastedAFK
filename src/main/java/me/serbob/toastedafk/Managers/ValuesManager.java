package me.serbob.toastedafk.Managers;

import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.AFKUtil;
import me.serbob.toastedafk.Utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ValuesManager {
    public static Map<Player, Integer> afkTimers = new ConcurrentHashMap<>(); // map of player timers
    public static int TIMEOUT_SECONDS=1200; // 20 minutes in seconds
    public static Location loc1;
    public static Location loc2;
    public static Location tempLoc1;
    public static Location tempLoc2;
    public static void loadConfigValues() {
        World world = Bukkit.getWorld(ToastedAFK.instance.getConfig().getString("region.locations.world"));
        loc1 = new Location(world,
                ToastedAFK.instance.getConfig().getDouble("region.locations.loc1.x"),
                ToastedAFK.instance.getConfig().getDouble("region.locations.loc1.y"),
                ToastedAFK.instance.getConfig().getDouble("region.locations.loc1.z"));
        loc2 = new Location(world,
                ToastedAFK.instance.getConfig().getDouble("region.locations.loc2.x"),
                ToastedAFK.instance.getConfig().getDouble("region.locations.loc2.y"),
                ToastedAFK.instance.getConfig().getDouble("region.locations.loc2.z"));
        Logger.log(Logger.LogLevel.INFO, AFKUtil.c("&aValues loaded!"));
    }
}
