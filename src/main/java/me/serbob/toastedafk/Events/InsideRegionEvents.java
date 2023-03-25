package me.serbob.toastedafk.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import static me.serbob.toastedafk.Managers.ValuesManager.levelTimer;

public class InsideRegionEvents implements Listener {
    @EventHandler
    public void anvilAndEnchantingOpen(InventoryClickEvent event) {
        if(event.getInventory().getType() == InventoryType.ANVIL||
                event.getInventory().getType() == InventoryType.ENCHANTING) {
            if(levelTimer.containsKey((Player) event.getWhoClicked())) {
                event.setCancelled(true);
            }
        }
    }
}