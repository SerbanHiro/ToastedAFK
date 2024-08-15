package me.serbob.toastedafk.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import static me.serbob.toastedafk.Managers.ValuesManager.playerStats;
import static me.serbob.toastedafk.Templates.CoreHelpers.showXpBar;

public class XPDenyEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        if (showXpBar && playerStats.containsKey(event.getPlayer())) {
            event.setAmount(0);
        }
    }
}