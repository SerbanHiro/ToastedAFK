package me.serbob.toastedafk;

import me.serbob.toastedafk.Functions.AFKCore;
import me.serbob.toastedafk.Managers.AFKManager;
import me.serbob.toastedafk.Managers.ValuesManager;
import me.serbob.toastedafk.Utils.Logger;
import me.serbob.toastedafk.Utils.Settings;
import me.serbob.toastedafk.Utils.UpdateChecker;
import org.bukkit.plugin.java.JavaPlugin;

public final class ToastedAFK extends JavaPlugin {
    public static ToastedAFK instance;
    @Override
    public void onEnable() {
        instance=this;
        saveDefaultConfig();
        ValuesManager.loadConfigValues();
        AFKManager.start();
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
