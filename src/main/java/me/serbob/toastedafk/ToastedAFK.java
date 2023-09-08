package me.serbob.toastedafk;

import me.serbob.toastedafk.Classes.Metrics;
import me.serbob.toastedafk.Managers.AFKManager;
import me.serbob.toastedafk.Managers.ValuesManager;
import me.serbob.toastedafk.API.PAPI.TimerPlaceHolder;
import me.serbob.toastedafk.Utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import static me.serbob.toastedafk.Templates.CoreHelpers.serverCrash;

public final class ToastedAFK extends JavaPlugin {
    public static ToastedAFK instance;
    public static File configFile;
    public static YamlConfiguration file;
    @Override
    public void onEnable() {
        instance=this;
        saveDefaultConfig();
        configFile = new File(getDataFolder(),"config.yml");
        enableMetrics();
        getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
            ValuesManager.loadConfigValues();
            AFKManager.start();
        });
        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new TimerPlaceHolder().register();
        }
    }
    @Override
    public void onDisable() {
        Logger.log(Logger.LogLevel.WARNING, "Server disabled! Giving player's exp back");
        serverCrash();
    }
    public void enableMetrics() {
        Metrics metrics = new Metrics(this,18463);
        metrics.addCustomChart(new Metrics.MultiLineChart("players_and_servers", new Callable<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> call() throws Exception {
                Map<String, Integer> valueMap = new HashMap<>();
                valueMap.put("servers", 1);
                valueMap.put("players", Bukkit.getOnlinePlayers().size());
                return valueMap;
            }
        }));
        metrics.addCustomChart(new Metrics.DrilldownPie("java_version", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            String javaVersion = System.getProperty("java.version");
            Map<String, Integer> entry = new HashMap<>();
            entry.put(javaVersion, 1);
            if (javaVersion.startsWith("1.7")) {
                map.put("Java 1.7", entry);
            } else if (javaVersion.startsWith("1.8")) {
                map.put("Java 1.8", entry);
            } else if (javaVersion.startsWith("1.9")) {
                map.put("Java 1.9", entry);
            } else {
                map.put("Other", entry);
            }
            return map;
        }));
    }
}
