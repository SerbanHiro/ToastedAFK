package me.serbob.toastedafk.API.events;

import me.serbob.toastedafk.objectholders.PlayerStats;
import me.serbob.toastedafk.events.custom.RewardsEvent;
import org.bukkit.entity.Player;

public class AFKRewardEvent extends RewardsEvent {
    public AFKRewardEvent(Player player, PlayerStats playerStats) {
        super(player, playerStats);
    }
}

