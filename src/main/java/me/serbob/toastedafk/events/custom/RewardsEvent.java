package me.serbob.toastedafk.events.custom;

import me.serbob.toastedafk.objectholders.PlayerStats;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class RewardsEvent extends PlayerEvent {
    private static HandlerList handlerList = new HandlerList();
    private final PlayerStats playerStats;
    public RewardsEvent(Player player, PlayerStats playerStats) {
        super(player);
        this.playerStats = playerStats;
    }
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
    public  static HandlerList getHandlerList() {
        return handlerList;
    }
    public PlayerStats getPlayerStats() {
        return this.playerStats;
    }
}
