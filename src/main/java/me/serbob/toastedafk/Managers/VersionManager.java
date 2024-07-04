package me.serbob.toastedafk.Managers;

import org.bukkit.Bukkit;

public class VersionManager {
    public static boolean isVersion1_12OrBelow() {
        boolean check;

        if(Bukkit.getBukkitVersion().contains("1.20.6") ||
        Bukkit.getBukkitVersion().contains("1.21")) {
            return false;
        }

        String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        String[] versionParts = serverVersion.substring(1).split("_");
        int majorVersion = Integer.parseInt(versionParts[0]);
        int minorVersion = Integer.parseInt(versionParts[1]);

        if (majorVersion < 1 || (majorVersion == 1 && minorVersion <= 12)) {
            check = true;
        } else {
            check = false;
        }
        return check;
    }
    public static boolean isVersion1_8OrAbove() {
        if(Bukkit.getBukkitVersion().contains("1.20.6") ||
                Bukkit.getBukkitVersion().contains("1.21")) {
            return true;
        }

        String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        String[] versionParts = serverVersion.substring(1).split("_");
        int majorVersion = Integer.parseInt(versionParts[0]);
        int minorVersion = Integer.parseInt(versionParts[1]);

        return majorVersion >= 1 && minorVersion >= 8;
    }
    public static boolean isVersion1_8() {
        if(Bukkit.getBukkitVersion().contains("1.20.6") ||
                Bukkit.getBukkitVersion().contains("1.21")) {
            return false;
        }

        String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        String[] versionParts = serverVersion.substring(1).split("_");
        int majorVersion = Integer.parseInt(versionParts[0]);
        int minorVersion = Integer.parseInt(versionParts[1]);

        return majorVersion == 1 && minorVersion == 8;
    }
}
