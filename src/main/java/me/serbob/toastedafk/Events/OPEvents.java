package me.serbob.toastedafk.Events;

import me.serbob.toastedafk.Functions.AFKCore;
import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.AFKUtil;
import me.serbob.toastedafk.Utils.Settings;
import me.serbob.toastedafk.Utils.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static me.serbob.toastedafk.Managers.AFKManager.shouldReset;
import static me.serbob.toastedafk.Managers.ValuesManager.*;

public class OPEvents implements Listener {
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        Player p = e.getPlayer();
        shouldReset = true;
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
    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(!playerStats.containsKey(player)) return;
        if(ToastedAFK.instance.getConfig().getBoolean("show_xp_bar")) {
            player.setLevel(playerStats.get(player).getLevelTimer());
            player.setExp(playerStats.get(player).getExpTimer());
        }
        playerStats.remove(player);
        if(ToastedAFK.instance.getConfig().getBoolean("bossbar.show")) {
            bossBar.removePlayer(player);
        }
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if(!ToastedAFK.instance.getConfig().getBoolean("show_xp_bar")) {
            if(!ToastedAFK.instance.getConfig().getBoolean("save_xp_inside_region")) {
                return;
            }
        }
        Player player = event.getEntity();
        if(playerStats.containsKey(player)) {
            playerStats.remove(player);
            if(ToastedAFK.instance.getConfig().getBoolean("bossbar.show")) {
                bossBar.removePlayer(player);
            }
            event.setDroppedExp(0);
        }
    }
}
