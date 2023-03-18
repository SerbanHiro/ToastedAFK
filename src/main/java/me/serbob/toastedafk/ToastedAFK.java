package me.serbob.toastedafk;

import me.serbob.toastedafk.Managers.AFKManager;
import me.serbob.toastedafk.Managers.ValuesManager;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

import static me.serbob.toastedafk.Managers.ValuesManager.afkTimers;

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
        // Plugin shutdown logic
    }
}
