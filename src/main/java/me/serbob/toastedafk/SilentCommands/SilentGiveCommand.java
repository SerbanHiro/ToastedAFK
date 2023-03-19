package me.serbob.toastedafk.SilentCommands;

import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.AFKUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.Yaml;

import java.io.StringReader;
import java.util.Map;

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
        if (victim.getInventory().firstEmpty() == -1) {
            victim.sendMessage(AFKUtil.c(ToastedAFK.instance.getConfig().getString("silent_player_inventory_full")));
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
            Material material = Material.valueOf(args[1]);
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
            victim.getInventory().addItem(item);
        } else if (args.length < 4) {
            amount = Integer.parseInt(args[2]);
            item.setAmount(amount);
            victim.getInventory().addItem(item);
        }
        return true;
    }
}
