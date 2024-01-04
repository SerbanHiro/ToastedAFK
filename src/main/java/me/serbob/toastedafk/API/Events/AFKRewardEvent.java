package me.serbob.toastedafk.API.Events;

import me.serbob.toastedafk.Classes.PlayerStats;
import me.serbob.toastedafk.Enums.CurrentMove;
import me.serbob.toastedafk.Events.Custom.RewardsEvent;
import me.serbob.toastedafk.Events.Custom.ToastedRegionEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class AFKRewardEvent extends RewardsEvent {
    public AFKRewardEvent(Player player, PlayerStats playerStats) {
        super(player, playerStats);
    }
}

