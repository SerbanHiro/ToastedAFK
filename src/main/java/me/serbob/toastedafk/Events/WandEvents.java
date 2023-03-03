package me.serbob.toastedafk.Events;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import static me.serbob.toastedafk.Managers.ValuesManager.tempLoc1;
import static me.serbob.toastedafk.Managers.ValuesManager.tempLoc2;

public class WandEvents implements Listener {
    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {
        try {
            if (event.getPlayer().getItemInHand() == null ||
                    !event.getPlayer().getItemInHand().hasItemMeta()) {
                return;
            }
            if (ChatColor.stripColor(event.getPlayer().getItemInHand().getItemMeta().getDisplayName()).equalsIgnoreCase("AFK Wand")) {
                if (!event.getPlayer().hasPermission("afkwand.use")) {
                    return;
                }
                if (event.getAction().name().contains("LEFT")) {
                    Block block = event.getClickedBlock();
                    if (block.getType().isSolid()) {
                        event.setCancelled(true);
                        tempLoc1 = block.getLocation();
                        event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "" + event.getClickedBlock().getLocation());
                    }

                } else if (event.getAction().name().contains("RIGHT")) {
                    Block block = event.getClickedBlock();
                    if (block.getType().isSolid()) {
                        tempLoc2 = block.getLocation();
                        event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "" + event.getClickedBlock().getLocation() + "");
                    }
                }
            }
        } catch (Exception ignored) {}
    }
}
