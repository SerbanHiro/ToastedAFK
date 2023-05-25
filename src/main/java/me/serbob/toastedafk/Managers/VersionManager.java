package me.serbob.toastedafk.Managers;

import org.bukkit.Bukkit;

public class VersionManager {
    public static boolean isVersion1_12OrBelow() {
        boolean check;

        // Manually check the server version
        String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        String[] versionParts = serverVersion.substring(1).split("_");
        int majorVersion = Integer.parseInt(versionParts[0]);
        int minorVersion = Integer.parseInt(versionParts[1]);

        // Check if the server version is 1.12 or below
        if (majorVersion < 1 || (majorVersion == 1 && minorVersion <= 12)) {
            check = true;
        } else {
            check = false;
        }
        return check;
    }
    public static boolean isVersion1_8OrAbove() {
        // Manually check the server version
        String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        String[] versionParts = serverVersion.substring(1).split("_");
        int majorVersion = Integer.parseInt(versionParts[0]);
        int minorVersion = Integer.parseInt(versionParts[1]);

        // Check if the server version is 1.8 or above
        return majorVersion >= 1 && minorVersion >= 8;
    }
    public static boolean isVersion1_8() {
        // Manually check the server version
        String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        String[] versionParts = serverVersion.substring(1).split("_");
        int majorVersion = Integer.parseInt(versionParts[0]);
        int minorVersion = Integer.parseInt(versionParts[1]);

        // Check if the server version is exactly 1.8
        return majorVersion == 1 && minorVersion == 8;
    }
}
