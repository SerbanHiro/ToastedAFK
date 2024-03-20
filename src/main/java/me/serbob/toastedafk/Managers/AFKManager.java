package me.serbob.toastedafk.Managers;

import me.serbob.toastedafk.Functions.AFKCore;
import me.serbob.toastedafk.Templates.CoreHelpers;
import me.serbob.toastedafk.Templates.LoadingScreen;
import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.AFKUtil;
import me.serbob.toastedafk.Utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.scheduler.BukkitScheduler;

import static me.serbob.toastedafk.Managers.ValuesManager.*;
import static me.serbob.toastedafk.Templates.CoreHelpers.schedulerTimer;

public class AFKManager {
    public static void start() {
        ConsoleErrorManager.checkErrors();
        registerPermissions();
        EventsManager.loadEvents();
        CommandsManager.loadCommands();
        TabCompletersManager.loadTabCompleters();

        CoreHelpers.readConfiguration();
        LoadingScreen.initializeLoadingScreen();

        startScheduler();
        //verifyScheduler();
        Logger.log(Logger.LogLevel.INFO, AFKUtil.c("Scheduler started!"));
    }
    public static void startScheduler() {
        BukkitScheduler scheduler = ToastedAFK.instance.getServer().getScheduler();
        scheduler.scheduleAsyncRepeatingTask(ToastedAFK.instance, () -> {
            //Bukkit.getScheduler().runTaskAsynchronously(ToastedAFK.instance, () -> {
                // Execute addOrRemovePlayers() in a separate thread
                AFKCore.getInstance().addOrRemovePlayers();
            //});

            /**Bukkit.getScheduler().runTaskAsynchronously(ToastedAFK.instance, () -> {
                // Execute regionCheck() in a separate thread
                AFKCore.getInstance().regionCheck();
            });*/
        }, 0L, 20L * schedulerTimer);
    }
    /**public static void verifyScheduler() {
        BukkitScheduler scheduler = ToastedAFK.instance.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(ToastedAFK.instance, () -> {
            playerTree = new PRTree<>(new AFKCore.PlayerMBRConverter(),branchFactor);
            playerTree.load(globalWorld.getPlayers());
        }, 0L, 20L * schedulerTimer);
    }*/
    public static void registerPermissions() {
        for (String perm : ToastedAFK.instance.getConfig().getConfigurationSection("afk_times").getKeys(false)) {
            String permissionName = "afk.perm." + perm.toLowerCase();
            if(ToastedAFK.instance.getServer().getPluginManager().getPermission(permissionName)==null) {
                Permission permission = new Permission(permissionName);
                ToastedAFK.instance.getServer().getPluginManager().addPermission(permission);
            }
        }
        Logger.log(Logger.LogLevel.INFO, AFKUtil.c("Permissions registered!"));
    }
}
