package me.serbob.toastedafk.API.Events;

import me.serbob.toastedafk.Enums.CurrentMove;
import me.serbob.toastedafk.Events.Custom.ToastedRegionEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;

public class OnRegionLeftEvent extends ToastedRegionEvent implements Cancellable {
    private boolean cancelled;
    private boolean cancellable;
    public OnRegionLeftEvent(Player player, CurrentMove movement, PlayerEvent parent) {
        super(player, movement, parent);
        this.cancelled = false;
        this.cancellable = true;
        if (movement == CurrentMove.SPAWN || movement == CurrentMove.DISCONNECT)
            this.cancellable = false;
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
