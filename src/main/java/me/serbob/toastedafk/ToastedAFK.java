package me.serbob.toastedafk;

import me.serbob.toastedafk.Managers.AFKManager;
import me.serbob.toastedafk.Managers.ValuesManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ToastedAFK extends JavaPlugin {
    public static ToastedAFK instance;
    @Override
    public void onEnable() {
        instance=this;
        saveDefaultConfig();
        AFKManager.start();
        ValuesManager.loadConfigValues();
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
