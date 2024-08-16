package me.serbob.toastedafk.managers;

import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.utils.ChatUtil;
import me.serbob.toastedafk.utils.Logger;
import me.serbob.toastedafk.utils.Settings;
import me.serbob.toastedafk.utils.UpdateChecker;

public class ConsoleErrorManager {
    public static void checkErrors() {
        checkForUpdate();
    }
    public static void checkForUpdate() {
        new UpdateChecker(ToastedAFK.instance, 108107).getLatestVersion(version -> {
            if (ToastedAFK.instance.getDescription().getVersion().equalsIgnoreCase(version)) {
                Logger.log(Logger.LogLevel.SUCCESS, ChatUtil.c("ToastedAFK is up to date!"));
            } else {
                Logger.log(Logger.LogLevel.OUTLINE, ChatUtil.c("⎯⎯⎯⎯⎯⎯⎯⎯⎯&r &x&f&f&a&d&6&1&lToastedAFK&r &8&l&m⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
                Logger.log(Logger.LogLevel.WARNING, ChatUtil.c("&cToastedAFK is outdated"));
                Logger.log(Logger.LogLevel.WARNING, ChatUtil.c("&7Newest version  &a'{newestVersion}'"
                        .replace("{newestVersion}",version)));
                Logger.log(Logger.LogLevel.WARNING, ChatUtil.c("&7Your version '&c{version}'"
                        .replace("{version}", ToastedAFK.instance.getDescription().getVersion())));
                Logger.log(Logger.LogLevel.WARNING, ChatUtil.c("&7Please update here &f&n{plugin_url}"
                        .replace("{plugin_url}",Settings.PLUGIN_URL)));
                Logger.log(Logger.LogLevel.OUTLINE,  ChatUtil.c("⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
            }
        });
    }
}
