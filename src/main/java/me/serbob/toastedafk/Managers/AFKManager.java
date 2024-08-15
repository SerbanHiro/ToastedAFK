package me.serbob.toastedafk.Managers;

import me.serbob.toastedafk.Functions.AFKCore;
import me.serbob.toastedafk.Templates.CoreHelpers;
import me.serbob.toastedafk.Templates.LoadingScreen;
import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.ChatUtil;
import me.serbob.toastedafk.Utils.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

import static me.serbob.toastedafk.Templates.CoreHelpers.schedulerTimer;

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