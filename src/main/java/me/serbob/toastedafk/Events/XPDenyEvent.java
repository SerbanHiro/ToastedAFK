package me.serbob.toastedafk.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import static me.serbob.toastedafk.Managers.ValuesManager.playerStats;
import static me.serbob.toastedafk.Templates.CoreHelpers.showXpBar;

public class XPDenyEvent implements Listener {
    @EventHandler
    public void disableXPUse(PlayerExpChangeEvent event) {
        Player player = event.getPlayer();
        if(playerStats.containsKey(player)) {
            if(showXpBar) {
                event.setAmount(0);
            }
        }
    }
}
