package me.serbob.toastedafk.utils;

import me.serbob.toastedafk.ToastedAFK;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class UpdateChecker {
    private ToastedAFK plugin;
    private int resourceId;

    public UpdateChecker(ToastedAFK plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public interface VersionCallback {
        void onVersionResult(String version);
    }

    public void getLatestVersion(VersionCallback callback) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource="
                    + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    callback.onVersionResult(scanner.next());
                }
            } catch (IOException exception) {
                this.plugin.getLogger().info("Cannot look for updates: " + exception.getMessage());
            }
        });
    }
}
