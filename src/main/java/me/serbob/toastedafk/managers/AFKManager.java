package me.serbob.toastedafk.managers;

import me.serbob.toastedafk.functions.AFKCore;
import me.serbob.toastedafk.templates.CoreHelpers;
import me.serbob.toastedafk.templates.LoadingScreen;
import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.utils.ChatUtil;
import me.serbob.toastedafk.utils.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

import static me.serbob.toastedafk.templates.CoreHelpers.schedulerTimer;

public class AFKManager {
    public static void start() {
        ConsoleErrorManager.checkErrors();
        registerPermissions();
        initializePluginComponents();
        startScheduler();
        Logger.log(Logger.LogLevel.INFO, ChatUtil.c("Scheduler started!"));
    }

    private static void initializePluginComponents() {
        EventsManager.loadEvents();
        CommandsManager.loadCommands();
        TabCompletersManager.loadTabCompleters();
        CoreHelpers.readConfiguration();
        LoadingScreen.initializeLoadingScreen();
    }

    public static void startScheduler() {
        BukkitScheduler scheduler = ToastedAFK.instance.getServer().getScheduler();
        scheduler.runTaskTimerAsynchronously(ToastedAFK.instance,
                AFKCore.getInstance()::addOrRemovePlayers, 0L, 20L * schedulerTimer);
    }

    public static void registerPermissions() {
        ConfigurationSection afkTimes = ToastedAFK.instance.getConfig().getConfigurationSection("afk_times");
        if (afkTimes == null) {
            Logger.log(Logger.LogLevel.WARNING, ChatUtil.c("No AFK times found in config!"));
            return;
        }

        PluginManager pluginManager = ToastedAFK.instance.getServer().getPluginManager();
        for (String perm : afkTimes.getKeys(false)) {
            String permissionName = "afk.perm." + perm.toLowerCase();
            if (pluginManager.getPermission(permissionName) == null) {
                pluginManager.addPermission(new Permission(permissionName));
            }
        }
        Logger.log(Logger.LogLevel.INFO, ChatUtil.c("Permissions registered!"));
    }
}