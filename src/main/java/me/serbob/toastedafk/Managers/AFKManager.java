package me.serbob.toastedafk.Managers;

import me.serbob.toastedafk.Functions.AFKCore;
import me.serbob.toastedafk.Templates.CoreHelpers;
import me.serbob.toastedafk.Templates.LoadingScreen;
import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.AFKUtil;
import me.serbob.toastedafk.Utils.Logger;
import org.bukkit.permissions.Permission;

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
        Logger.log(Logger.LogLevel.INFO, AFKUtil.c("Scheduler started!"));
    }
    public static void startScheduler() {
        ToastedAFK.instance.getServer().getScheduler().scheduleSyncRepeatingTask(ToastedAFK.instance, () -> {
            AFKCore.getInstance().addOrRemovePlayers();
            AFKCore.getInstance().regionCheck();
        }, 0L, 20L);
    }
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
