package me.serbob.toastedafk.Events;

import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.AFKUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import static me.serbob.toastedafk.Managers.ValuesManager.afkTimers;

public class DenyCommands implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void PreProcessCommand(PlayerCommandPreprocessEvent event) {
        if(afkTimers.containsKey(event.getPlayer())) {
            String cmd = event.getMessage();
            if (ToastedAFK.instance.getConfig().getStringList("denied_commands").contains(cmd)) {
                event.getPlayer().sendMessage(AFKUtil.c(ToastedAFK.instance.getConfig().getString("on_command_deny")));
                event.setCancelled(true);
            }
        }
    }
}
