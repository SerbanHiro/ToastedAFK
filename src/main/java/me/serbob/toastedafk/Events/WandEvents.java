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

import java.lang.reflect.Method;
import java.util.List;

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
                List<String> lore = event.getPlayer().getItemInHand().getItemMeta().getLore();
                int ok = 0;
                if (lore != null) {
                    for (String line : lore) {
                        if (ChatColor.stripColor(line).equalsIgnoreCase("Set position 1 and 2 with this magic wand!")) {
                            ok = 1;
                        }
                    }
                }
                if (ok == 0) {
                    return;
                }
                if (!event.getPlayer().hasPermission("afkwand.use")) {
                    return;
                }
                if (event.getAction().name().contains("LEFT")) {
                    Block block = event.getClickedBlock();
                    event.setCancelled(true);
                    tempLoc1 = block.getLocation();
                    event.getPlayer().sendMessage(AFKUtil.c("&7(&cAFK&7) Position 1! x: &c" + event.getClickedBlock().getX() +
                            " &7y: &c" + event.getClickedBlock().getY() +
                            " &7z: &c" + event.getClickedBlock().getZ()));

                } else if (event.getAction().name().contains("RIGHT")) {
                    int ok2 = 0;
                    try {
                        Method getHand = event.getClass().getMethod("getHand");
                        EquipmentSlot hand = (EquipmentSlot) getHand.invoke(event);
                        ok2 = 1;
                        if (hand.equals(EquipmentSlot.HAND)) {
                            Block block = event.getClickedBlock();
                            tempLoc2 = block.getLocation();
                            event.getPlayer().sendMessage(AFKUtil.c("&7(&cAFK&7) Position 2! x: &c" + event.getClickedBlock().getX() +
                                    " &7y: &c" + event.getClickedBlock().getY() +
                                    " &7z: &c" + event.getClickedBlock().getZ()));
                        }
                    } catch (Exception ignored) {}
                    if (ok2 == 0) {
                        Block block = event.getClickedBlock();
                        tempLoc2 = block.getLocation();
                        event.getPlayer().sendMessage(AFKUtil.c("&7(&cAFK&7) Position 2! x: &c" + event.getClickedBlock().getX() +
                                " &7y: &c" + event.getClickedBlock().getY() +
                                " &7z: &c" + event.getClickedBlock().getZ()));
                    }
                }
            }
        }catch (Exception ignored) {}
    }
}
