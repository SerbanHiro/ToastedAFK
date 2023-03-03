package me.serbob.toastedafk.Commands;

import me.serbob.toastedafk.Utils.AFKUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class AFKWandCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You are not a player!");
            return false;
        }
        if(!sender.hasPermission("afkwand.use")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return false;
        }
        Player player = (Player) sender;

        ItemStack wand;
        wand = new ItemStack(Material.STONE_AXE);
        ItemMeta wandMeta = wand.getItemMeta();
        wandMeta.setDisplayName(AFKUtil.c("&dAFK Wand"));
        wand.setItemMeta(wandMeta);

        player.getInventory().addItem(wand);
        player.sendMessage(AFKUtil.c("&aAFK Wand added!"));
        return true;
    }
}
