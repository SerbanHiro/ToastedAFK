package me.serbob.toastedafk.Events;

import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.AFKUtil;
import me.serbob.toastedafk.Utils.Settings;
import me.serbob.toastedafk.Utils.UpdateChecker;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OPJoined implements Listener {
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (ToastedAFK.instance.getConfig().getBoolean("check_for_update")) {
            if (p.hasPermission("afk.update")) {
                //PLEASE REPLACE THE RESOURCE ID WITH YOUR SPIGOT RESOURCE
                new UpdateChecker(ToastedAFK.instance, 108107).getLatestVersion(version -> {
                    if (!ToastedAFK.instance.getDescription().getVersion().equalsIgnoreCase(version)) {
                        p.sendMessage(AFKUtil.c("&8&l&m⎯⎯⎯⎯⎯⎯⎯⎯⎯&r &x&f&f&a&d&6&1&lToastedAFK&r &8&l&m⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
                        p.sendMessage(AFKUtil.c("&c&lWARNING &8» &cToastedAFK is outdated"));
                        p.sendMessage(AFKUtil.c("&c&lWARNING &8» &7Newest version  &a'{newestVersion}'"
                                .replace("{newestVersion}",version)));
                        p.sendMessage(AFKUtil.c("&c&lWARNING &8» &7Your version '&c{version}'"
                                .replace("{version}",Settings.VERSION)));
                        p.sendMessage(AFKUtil.c("&c&lWARNING &8» &7Please update here &f&n{plugin_url}"
                                .replace("{plugin_url}",Settings.PLUGIN_URL)));
                        p.sendMessage(AFKUtil.c("&8&l&m⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));                    }
                });
            }
        }
    }
}
