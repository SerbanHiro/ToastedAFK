package me.serbob.toastedafk.Events;

import me.serbob.toastedafk.Utils.RegionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import static me.serbob.toastedafk.Managers.ValuesManager.*;
import static me.serbob.toastedafk.Templates.CoreHelpers.removePlayer;

public class InsideRegionEvents implements Listener {
    @EventHandler
    public void anvilAndEnchantingOpen(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(event.getInventory().getType() == InventoryType.ANVIL||
                event.getInventory().getType() == InventoryType.ENCHANTING) {
            if(playerStats.containsKey(player)&&
            playerStats.get((Player) event.getWhoClicked()).isXpEnabled()) {
                if(RegionUtils.playerInCubiod(player.getLocation(),loc1,loc2)) {
                    event.setCancelled(true);
                } else {
                    removePlayer(player);
                }
            }
        }
    }
}
