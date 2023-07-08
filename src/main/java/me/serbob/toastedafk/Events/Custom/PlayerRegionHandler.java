package me.serbob.toastedafk.Events.Custom;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static me.serbob.toastedafk.Templates.CoreHelpers.addPlayer;
import static me.serbob.toastedafk.Templates.CoreHelpers.removePlayer;

public class PlayerRegionHandler implements Listener {
    @EventHandler
    public void onRegionEnter(OnRegionEnteredEvent event) {
        Player player = event.getPlayer();
        addPlayer(player);
    }

    @EventHandler
    public void onRegionLeft(OnRegionLeftEvent event) {
        Player player = event.getPlayer();
        removePlayer(player);
    }
}
