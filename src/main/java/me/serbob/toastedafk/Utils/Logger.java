package me.serbob.toastedafk.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Logger {
    public static void log(LogLevel level, String message) {
        if (message == null) return;

        switch (level) {
            case ERROR:
                Bukkit.getConsoleSender().sendMessage(AFKUtil.c("&8[&c&lERROR&r&8] &f" + message));
                break;
            case WARNING:
                Bukkit.getConsoleSender().sendMessage(AFKUtil.c("&c&k&lWARNING &8» &f" + message));
                break;
            case INFO:
                Bukkit.getConsoleSender().sendMessage(AFKUtil.c("&x&f&f&a&d&6&1&lINFO&r &8» &f" + message));
                break;
            case SUCCESS:
                Bukkit.getConsoleSender().sendMessage(AFKUtil.c("&a&lSUCCESS &8» &f" + message));
                break;
            case OUTLINE:
                Bukkit.getConsoleSender().sendMessage(AFKUtil.c("&8&l&m{message}"
                        .replace("{message}",message)));
                break;
        }
    }

    public enum LogLevel { ERROR, WARNING, INFO, SUCCESS, OUTLINE }
}
