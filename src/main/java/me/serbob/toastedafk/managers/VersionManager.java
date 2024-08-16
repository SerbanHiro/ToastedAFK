package me.serbob.toastedafk.managers;

import org.bukkit.Bukkit;

public class VersionManager {
    private static final String[] FUTURE_VERSIONS = {"1.20.6", "1.21", "1.21.1"};
    private static final VersionInfo SERVER_VERSION;

    static {
        SERVER_VERSION = parseServerVersion();
    }

    public static boolean isVersion1_12OrBelow() {
        if (isFutureVersion()) return false;
        return SERVER_VERSION.majorVersion < 1 ||
                (SERVER_VERSION.majorVersion == 1 && SERVER_VERSION.minorVersion <= 12);
    }

    public static boolean isVersion1_8OrAbove() {
        if (isFutureVersion()) return true;
        return SERVER_VERSION.majorVersion > 1 ||
                (SERVER_VERSION.majorVersion == 1 && SERVER_VERSION.minorVersion >= 8);
    }

    public static boolean isVersion1_8() {
        if (isFutureVersion()) return false;
        return SERVER_VERSION.majorVersion == 1 && SERVER_VERSION.minorVersion == 8;
    }

    private static boolean isFutureVersion() {
        String version = Bukkit.getBukkitVersion();
        for (String futureVersion : FUTURE_VERSIONS) {
            if (version.startsWith(futureVersion)) return true;
        }
        return false;
    }

    private static VersionInfo parseServerVersion() {
        String bukkitVersion = Bukkit.getBukkitVersion();
        String[] versionParts = bukkitVersion.split("-")[0].split("\\.");

        if (versionParts.length >= 2) {
            try {
                int majorVersion = Integer.parseInt(versionParts[0]);
                int minorVersion = Integer.parseInt(versionParts[1]);
                return new VersionInfo(majorVersion, minorVersion);
            } catch (NumberFormatException e) {
                return new VersionInfo(1, 0);
            }
        }

        return new VersionInfo(1, 0);
    }

    private static class VersionInfo {
        final int majorVersion;
        final int minorVersion;

        VersionInfo(int majorVersion, int minorVersion) {
            this.majorVersion = majorVersion;
            this.minorVersion = minorVersion;
        }
    }
}