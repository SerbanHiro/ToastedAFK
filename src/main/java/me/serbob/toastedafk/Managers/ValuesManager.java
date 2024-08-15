package me.serbob.toastedafk.Managers;

import me.serbob.toastedafk.Classes.PlayerStats;
import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.ChatUtil;
import me.serbob.toastedafk.Utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ValuesManager {
    public static final Map<Player, PlayerStats> playerStats = new ConcurrentHashMap<>();
    public static int TIMEOUT_SECONDS = 1200; // 20 minutes in seconds
    public static int DEFAULT_AFK_TIME;
    public static World globalWorld;
    public static Location loc1, loc2, tempLoc1, tempLoc2;
    public static BossBar bossBar;
    public static BarColor barColor;
    public static BarStyle barStyle;

    public static void loadConfigValues() {
        Configuration config = ToastedAFK.instance.getConfig();

        loadWorldAndLocations(config);
        loadAFKTime(config);
        loadBossBar(config);

        Logger.log(Logger.LogLevel.INFO, ChatUtil.c("&aValues loaded!"));
    }

    private static void loadWorldAndLocations(Configuration config) {
        String worldName = config.getString("region.locations.world");
        globalWorld = Objects.requireNonNull(Bukkit.getWorld(worldName), "World not found: " + worldName);

        loc1 = loadLocation(config, "region.locations.loc1");
        loc2 = loadLocation(config, "region.locations.loc2");
    }

    private static Location loadLocation(Configuration config, String path) {
        return new Location(globalWorld,
                config.getDouble(path + ".x"),
                config.getDouble(path + ".y"),
                config.getDouble(path + ".z"));
    }

    private static void loadAFKTime(Configuration config) {
        DEFAULT_AFK_TIME = config.getInt("default_afk_time");
    }

    private static void loadBossBar(Configuration config) {
        if (config.getBoolean("bossbar.show")) {
            barColor = BarColor.valueOf(config.getString("bossbar.color", "WHITE"));
            barStyle = BarStyle.valueOf(config.getString("bossbar.style", "SOLID"));
            String bossBarText = ChatUtil.c(config.getString("bossbar.text", ""));
            bossBar = Bukkit.createBossBar(bossBarText, barColor, barStyle);
        }
    }
}