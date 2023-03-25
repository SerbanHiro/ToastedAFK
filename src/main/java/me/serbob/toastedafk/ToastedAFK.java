package me.serbob.toastedafk;

import jdk.jpackage.internal.Log;
import me.serbob.toastedafk.Managers.AFKManager;
import me.serbob.toastedafk.Managers.ValuesManager;
import me.serbob.toastedafk.Utils.Logger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static me.serbob.toastedafk.Managers.ValuesManager.*;

public final class ToastedAFK extends JavaPlugin {
    public static ToastedAFK instance;
    public static File configFile;
    public static YamlConfiguration file;
    @Override
    public void onEnable() {
        instance=this;
        saveDefaultConfig();
        configFile = new File(getDataFolder(),"config.yml");
        getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
            ValuesManager.loadConfigValues();
            AFKManager.start();
        });
    }
    @Override
    public void onDisable() {
        Logger.log(Logger.LogLevel.WARNING, "Server disabled! Giving player's exp back");
        for(Map.Entry<Player, Integer> entry: levelTimer.entrySet()) {
            Player player = entry.getKey();
            int level = entry.getValue();
            player.setLevel(level);
            levelTimer.remove(player);
        }
        for(Map.Entry<Player, Float> entry : expTimer.entrySet()) {
            Player player = entry.getKey();
            Float exp = entry.getValue();
            player.setExp(exp);
            expTimer.remove(player);
        }
    }
}
