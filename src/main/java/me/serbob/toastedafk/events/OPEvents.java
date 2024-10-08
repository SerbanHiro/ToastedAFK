package me.serbob.toastedafk.events;

import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.utils.ChatUtil;
import me.serbob.toastedafk.utils.Settings;
import me.serbob.toastedafk.utils.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import static me.serbob.toastedafk.managers.ValuesManager.*;

public class OPEvents implements Listener {
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (ToastedAFK.instance.getConfig().getBoolean("check_for_update")) {
            if (p.hasPermission("afk.update")) {
                //PLEASE REPLACE THE RESOURCE ID WITH YOUR SPIGOT RESOURCE
                new UpdateChecker(ToastedAFK.instance, 108107).getLatestVersion(version -> {
                    if (!ToastedAFK.instance.getDescription().getVersion().equalsIgnoreCase(version)) {
                        p.sendMessage(ChatUtil.c("&8&l&m⎯⎯⎯⎯⎯⎯⎯⎯⎯&r &x&f&f&a&d&6&1&lToastedAFK&r &8&l&m⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
                        p.sendMessage(ChatUtil.c("&c&lWARNING &8» &cToastedAFK is outdated"));
                        p.sendMessage(ChatUtil.c("&c&lWARNING &8» &7Newest version  &a'{newestVersion}'"
                                .replace("{newestVersion}",version)));
                        p.sendMessage(ChatUtil.c("&c&lWARNING &8» &7Your version '&c{version}'"
                                .replace("{version}", ToastedAFK.instance.getDescription().getVersion())));
                        p.sendMessage(ChatUtil.c("&c&lWARNING &8» &7Please update here &f&n{plugin_url}"
                                .replace("{plugin_url}",Settings.PLUGIN_URL)));
                        p.sendMessage(ChatUtil.c("&8&l&m⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));                    }
                });
            }
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
