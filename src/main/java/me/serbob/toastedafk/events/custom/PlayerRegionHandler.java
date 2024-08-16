package me.serbob.toastedafk.events.custom;

import me.serbob.toastedafk.API.events.AFKRewardEvent;
import me.serbob.toastedafk.API.events.OnRegionEnteredEvent;
import me.serbob.toastedafk.API.events.OnRegionLeftEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static me.serbob.toastedafk.templates.CoreHelpers.addPlayer;
import static me.serbob.toastedafk.templates.CoreHelpers.removePlayer;

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

    @EventHandler
    public void onRewardReceived(AFKRewardEvent event) {

    }
}
