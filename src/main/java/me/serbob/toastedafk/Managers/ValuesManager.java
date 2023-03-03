package me.serbob.toastedafk.Managers;

import me.serbob.toastedafk.ToastedAFK;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ValuesManager {
    public static Map<Player, Integer> afkTimers = new ConcurrentHashMap<>(); // map of player timers
    public static String REGION_NAME = "AFK";
    public static int TIMEOUT_SECONDS=1200; // 20 minutes in seconds
    public static void loadConfigValues() {
        REGION_NAME=ToastedAFK.instance.getConfig().getString("region");
    }
}
