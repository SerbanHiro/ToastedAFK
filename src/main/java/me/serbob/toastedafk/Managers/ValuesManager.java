package me.serbob.toastedafk.Managers;

import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.AFKUtil;
import me.serbob.toastedafk.Utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ValuesManager {
    public static Map<Player, Integer> afkTimers = new ConcurrentHashMap<>(); // map of player timers
    public static Map<Player, Integer> levelTimer = new ConcurrentHashMap<>();
    public static Map<Player, Float> expTimer = new ConcurrentHashMap<>();
    public static int TIMEOUT_SECONDS=1200; // 20 minutes in seconds
    public static int DEFAULT_AFK_TIME;
    public static Location loc1;
    public static Location loc2;
    public static Location tempLoc1;
    public static Location tempLoc2;
    public static BossBar bossBar;
    public static BarColor barColor;
    public static BarStyle barStyle;
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
        DEFAULT_AFK_TIME = ToastedAFK.instance.getConfig().getInt("default_afk_time");
        barColor = BarColor.valueOf(ToastedAFK.instance.getConfig().getString("bossbar.color"));
        barStyle = BarStyle.valueOf(ToastedAFK.instance.getConfig().getString("bossbar.style"));
        bossBar = Bukkit.createBossBar(AFKUtil.c(ToastedAFK.instance.getConfig().getString("bossbar.text")), barColor, barStyle);
        Logger.log(Logger.LogLevel.INFO, AFKUtil.c("&aValues loaded!"));
    }
}
