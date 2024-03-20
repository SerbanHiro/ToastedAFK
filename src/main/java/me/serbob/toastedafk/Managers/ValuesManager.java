package me.serbob.toastedafk.Managers;

import me.serbob.toastedafk.Classes.PlayerStats;
import me.serbob.toastedafk.Functions.AFKCore;
import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.AFKUtil;
import me.serbob.toastedafk.Utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ValuesManager {
    public static Map<Player, PlayerStats> playerStats = new ConcurrentHashMap<>();
    public static int TIMEOUT_SECONDS=1200; // 20 minutes in seconds
    public static int DEFAULT_AFK_TIME;
    public static World globalWorld;
    public static Location loc1;
    public static Location loc2;
    public static Location tempLoc1;
    public static Location tempLoc2;
    public static BossBar bossBar;
    public static BarColor barColor;
    public static BarStyle barStyle;
    //public static PRTree<Player> playerTree;
    //public static SimpleMBR bounds;
    //public static int branchFactor;
    public static void loadConfigValues() {
        globalWorld = Bukkit.getWorld(Objects.requireNonNull(ToastedAFK.instance.getConfig().getString("region.locations.world")));
        loc1 = new Location(globalWorld,
                ToastedAFK.instance.getConfig().getDouble("region.locations.loc1.x"),
                ToastedAFK.instance.getConfig().getDouble("region.locations.loc1.y"),
                ToastedAFK.instance.getConfig().getDouble("region.locations.loc1.z"));
        loc2 = new Location(globalWorld,
                ToastedAFK.instance.getConfig().getDouble("region.locations.loc2.x"),
                ToastedAFK.instance.getConfig().getDouble("region.locations.loc2.y"),
                ToastedAFK.instance.getConfig().getDouble("region.locations.loc2.z"));
        DEFAULT_AFK_TIME = ToastedAFK.instance.getConfig().getInt("default_afk_time");
        if(ToastedAFK.instance.getConfig().getBoolean("bossbar.show")) {
            barColor = BarColor.valueOf(ToastedAFK.instance.getConfig().getString("bossbar.color"));
            barStyle = BarStyle.valueOf(ToastedAFK.instance.getConfig().getString("bossbar.style"));
            bossBar = Bukkit.createBossBar(AFKUtil.c(ToastedAFK.instance.getConfig().getString("bossbar.text")), barColor, barStyle);
        }

        //branchFactor = ToastedAFK.instance.getConfig().getInt("branchFactor");
        //bounds = playerBounds(loc1, loc2);
        //playerTree = new PRTree<>(new AFKCore.PlayerMBRConverter(),branchFactor);
        //playerTree.load(globalWorld.getPlayers());
        Logger.log(Logger.LogLevel.INFO, AFKUtil.c("&aValues loaded!"));
    }
    /**private static SimpleMBR playerBounds(Location loc1, Location loc2) {
        double minX = Math.min(loc1.getX(), loc2.getX());
        double minY = Math.min(loc1.getY(), loc2.getY());
        double minZ = Math.min(loc1.getZ(), loc2.getZ());
        double maxX = Math.max(loc1.getX(), loc2.getX());
        double maxY = Math.max(loc1.getY(), loc2.getY());
        double maxZ = Math.max(loc1.getZ(), loc2.getZ());

        return new SimpleMBR(minX, maxX, minY, maxY, minZ, maxZ);
    }*/
}
