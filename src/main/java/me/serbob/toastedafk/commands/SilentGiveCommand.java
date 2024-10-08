package me.serbob.toastedafk.commands;

import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SilentGiveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("silentgive.use")) {
            return false;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /silentgive {player} <item> <amount>");
            return false;
        }
        Player victim = Bukkit.getPlayer(args[0]);
        if (victim == null) {
            sender.sendMessage(ChatColor.RED + "Invalid player!");
            return false;
        }
        int amount = 1;
        String itemString = args[1];
        ItemStack item;
        if(itemString.startsWith("{")&&itemString.endsWith("}")) {
            itemString = itemString.replace("{","");
            itemString = itemString.replace("}","");
        }
        if(ToastedAFK.instance.getConfig().getConfigurationSection("items").getKeys(false).contains(itemString)) {
            item = ToastedAFK.instance.getConfig().getItemStack("items."+itemString);
        } else {
            Material material = Material.valueOf(args[1].toUpperCase());
            if(material==null) {
                sender.sendMessage(ChatColor.RED + "INVALID ITEM");
                return false;
            }
            item = new ItemStack(material);
        }
        if (item == null) {
            sender.sendMessage(ChatColor.RED + "INVALID ITEM '" + ChatColor.WHITE + itemString + ChatColor.RED + "'");
            return false;
        }
        if (args.length < 3) {
            if (!victim.getInventory().addItem(item).isEmpty() && victim.getInventory().firstEmpty() == -1) {
                victim.sendMessage(ChatUtil.c(ToastedAFK.instance.getConfig().getString("silent_player_inventory_full")));
                return false;
            }
        } else if (args.length < 4) {
            amount = Integer.parseInt(args[2]);
            item.setAmount(amount);
            if (!victim.getInventory().addItem(item).isEmpty() && victim.getInventory().firstEmpty() == -1) {
                victim.sendMessage(ChatUtil.c(ToastedAFK.instance.getConfig().getString("silent_player_inventory_full")));
                return false;
            }
        }
        return true;
    }
}
