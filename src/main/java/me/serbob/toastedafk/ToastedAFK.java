package me.serbob.toastedafk;

import jdk.jpackage.internal.Log;
import me.serbob.toastedafk.Classes.PlayerStats;
import me.serbob.toastedafk.Managers.AFKManager;
import me.serbob.toastedafk.Managers.ValuesManager;
import me.serbob.toastedafk.Placeholders.TimerPlaceHolder;
import me.serbob.toastedafk.Utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static me.serbob.toastedafk.Managers.ValuesManager.*;
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
}
