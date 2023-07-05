package me.serbob.toastedafk.Events.Custom;

import me.serbob.toastedafk.Enums.CurrentMove;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class ToastedRegionEvent extends PlayerEvent {
    private static HandlerList handlerList = new HandlerList();
    private CurrentMove movementWay;
    private PlayerEvent parentEvent;
    public ToastedRegionEvent(@NotNull Player player, CurrentMove movementWay, PlayerEvent parent) {
        super(player);
        this.movementWay = movementWay;
        this.parentEvent = parent;
    }
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
    public  static HandlerList getHandlerList() {
        return handlerList;
    }
    public CurrentMove getMovementWay() {
        return this.movementWay;
    }
    public PlayerEvent getParentEvent() {
        return this.parentEvent;
    }
}
