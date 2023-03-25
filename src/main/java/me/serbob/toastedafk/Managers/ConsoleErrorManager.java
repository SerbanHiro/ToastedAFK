package me.serbob.toastedafk.Managers;

import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.AFKUtil;
import me.serbob.toastedafk.Utils.Logger;
import me.serbob.toastedafk.Utils.Settings;
import me.serbob.toastedafk.Utils.UpdateChecker;

import static org.bukkit.Bukkit.getLogger;

public class ConsoleErrorManager {
    public static void checkErrors() {
        checkForUpdate();
    }
    public static void checkForUpdate() {
        new UpdateChecker(ToastedAFK.instance, 108107).getLatestVersion(version -> {
            if (ToastedAFK.instance.getDescription().getVersion().equalsIgnoreCase(version)) {
                Logger.log(Logger.LogLevel.SUCCESS, AFKUtil.c("ToastedAFK is up to date!"));
            } else {
                Logger.log(Logger.LogLevel.OUTLINE, AFKUtil.c("⎯⎯⎯⎯⎯⎯⎯⎯⎯&r &x&f&f&a&d&6&1&lToastedAFK&r &8&l&m⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
                Logger.log(Logger.LogLevel.WARNING, AFKUtil.c("&cToastedAFK is outdated"));
                Logger.log(Logger.LogLevel.WARNING, AFKUtil.c("&7Newest version  &a'{newestVersion}'"
                        .replace("{newestVersion}",version)));
                Logger.log(Logger.LogLevel.WARNING,AFKUtil.c("&7Your version '&c{version}'"
                        .replace("{version}", Settings.VERSION)));
                Logger.log(Logger.LogLevel.WARNING,AFKUtil.c("&7Please update here &f&n{plugin_url}"
                        .replace("{plugin_url}",Settings.PLUGIN_URL)));
                Logger.log(Logger.LogLevel.OUTLINE,  AFKUtil.c("⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
            }
        });
    }
}
