package me.serbob.toastedafk.API.Events;

import me.serbob.toastedafk.Enums.CurrentMove;
import me.serbob.toastedafk.Events.Custom.ToastedRegionEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class OnRegionEnteredEvent extends ToastedRegionEvent implements Cancellable {
    private boolean cancelled;
    private boolean cancellable;

    public OnRegionEnteredEvent(@NotNull Player player, CurrentMove movementWay, PlayerEvent parent) {
        super(player, movementWay, parent);
        this.cancelled = false;
        this.cancellable = true;
        if(movementWay == CurrentMove.SPAWN || movementWay == CurrentMove.DISCONNECT) {
            this.cancellable = false;
        }
    }
    @Override
    public void setCancelled(boolean cancelled) {
        if (!this.cancellable)
            return;
        this.cancelled = cancelled;
    }
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }
}
