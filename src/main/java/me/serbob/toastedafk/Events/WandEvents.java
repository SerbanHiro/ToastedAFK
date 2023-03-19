package me.serbob.toastedafk.Events;

import me.serbob.toastedafk.Utils.AFKUtil;
import me.serbob.toastedafk.Utils.RegionUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

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
            if (event.getPlayer().getItemInHand().getItemMeta().getLocalizedName().equalsIgnoreCase("AFKWand")) {
                if (!event.getPlayer().hasPermission("afkwand.use")) {
                    return;
                }
                if (event.getAction().name().contains("LEFT")) {
                    Block block = event.getClickedBlock();
                    event.setCancelled(true);
                    tempLoc1 = block.getLocation();
                    event.getPlayer().sendMessage(AFKUtil.c("&7(&cAFK&7) Position 1! x: &c" + event.getClickedBlock().getX()+
                            " &7y: &c" + event.getClickedBlock().getY()+
                            " &7z: &c" + event.getClickedBlock().getZ()));

                } else if (event.getAction().name().contains("RIGHT")) {
                    EquipmentSlot e = event.getHand();
                    if(e.equals(EquipmentSlot.HAND)) {
                        Block block = event.getClickedBlock();
                        tempLoc2 = block.getLocation();
                        event.getPlayer().sendMessage(AFKUtil.c("&7(&cAFK&7) Position 2! x: &c" + event.getClickedBlock().getX()+
                                " &7y: &c" + event.getClickedBlock().getY()+
                                " &7z: &c" + event.getClickedBlock().getZ()));
                    }
                }
            }
        } catch (Exception ignored) {}
    }
}
