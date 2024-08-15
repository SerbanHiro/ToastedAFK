package me.serbob.toastedafk.Events;

import me.serbob.toastedafk.Classes.PlayerStats;
import me.serbob.toastedafk.Utils.RegionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import static me.serbob.toastedafk.Managers.ValuesManager.*;
import static me.serbob.toastedafk.Templates.CoreHelpers.removePlayer;

public class InsideRegionEvents implements Listener {
    private final InventoryType[] RESTRICTED_INVENTORIES = {
            InventoryType.ANVIL, InventoryType.ENCHANTING
    };

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        InventoryType inventoryType = event.getInventory().getType();

        if (isRestrictedInventory(inventoryType) && isPlayerAFKWithXPEnabled(player)) {
            handleInventoryInteraction(player, event);
        }
    }

    private boolean isRestrictedInventory(InventoryType type) {
        for (InventoryType restrictedType : RESTRICTED_INVENTORIES) {
            if (type == restrictedType) {
                return true;
            }
        }
        return false;
    }

    private boolean isPlayerAFKWithXPEnabled(Player player) {
        PlayerStats stats = playerStats.get(player);
        return stats != null && stats.isXpEnabled();
    }

    private void handleInventoryInteraction(Player player, InventoryClickEvent event) {
        if (RegionUtils.playerInCubiod(player.getLocation(), loc1, loc2)) {
            event.setCancelled(true);
        } else {
            removePlayer(player);
        }
    }
}