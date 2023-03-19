package me.serbob.toastedafk.Managers;

import me.serbob.toastedafk.Functions.AFKCore;
import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.AFKUtil;
import me.serbob.toastedafk.Utils.Logger;
import me.serbob.toastedafk.Utils.RegionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static me.serbob.toastedafk.Managers.ValuesManager.*;
import static me.serbob.toastedafk.Managers.ValuesManager.afkTimers;
import static me.serbob.toastedafk.Utils.RegionUtils.playerInCubiod;

public class AFKManager {
    public static void start() {
        ConsoleErrorManager.checkErrors();
        registerPermissions();
        EventsManager.loadEvents();
        CommandsManager.loadCommands();
        TabCompletersManager.loadTabCompleters();
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
        ConfigurationSection permSection = ToastedAFK.instance.getConfig().getConfigurationSection("afk_times");
        Set<String> perms = permSection.getKeys(false);
        for (String perm : perms) {
            String permissionName = "afk.perm." + perm.toLowerCase();
            if(ToastedAFK.instance.getServer().getPluginManager().getPermission(permissionName)==null) {
                Permission permission = new Permission(permissionName);
                ToastedAFK.instance.getServer().getPluginManager().addPermission(permission);
            }
        }
        Logger.log(Logger.LogLevel.INFO, AFKUtil.c("Permissions registered!"));
    }
}
