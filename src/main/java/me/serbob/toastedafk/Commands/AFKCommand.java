package me.serbob.toastedafk.Commands;

import me.serbob.toastedafk.Managers.ValuesManager;
import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.AFKUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;

import static me.serbob.toastedafk.Managers.AFKManager.startScheduler;
import static me.serbob.toastedafk.Managers.AFKManager.taskID;
import static me.serbob.toastedafk.Managers.ValuesManager.*;
import static me.serbob.toastedafk.Managers.ValuesManager.tempLoc2;
import static me.serbob.toastedafk.ToastedAFK.configFile;
import static me.serbob.toastedafk.ToastedAFK.file;

public class AFKCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You are not a player!");
            return false;
        }
        if(args.length<1) {
            sender.sendMessage(ChatColor.RED + "Usage: /tafk <wand/save/reload>");
            return false;
        }
        if(args[0].equalsIgnoreCase("save")) {
            if(sender.hasPermission("afk.save")) {
                saveCommand((Player) sender);
            } else {
                noPermission(sender);
                return false;
            }
        } else if(args[0].equalsIgnoreCase("wand")) {
            if(sender.hasPermission("afk.wand")) {
                wandCommand((Player) sender);
            } else {
                noPermission(sender);
                return false;
            }
        } else if(args[0].equalsIgnoreCase("reload")) {
            //if(sender.hasPermission("afk.reload")) {
                configFile = new File(ToastedAFK.instance.getDataFolder(),"config.yml");
                file = YamlConfiguration.loadConfiguration(configFile);
                try {
                    file.save(configFile);
                    file.load(configFile);
                } catch (IOException | InvalidConfigurationException e) {
                    throw new RuntimeException(e);
                }
                afkTimers.clear();
                ValuesManager.loadConfigValues();
                //reloadCommand((Player) sender);
            //} else {
              //  noPermission(sender);
              //  return false;
            //}
        }
        return true;
    }
    public void noPermission(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
    }
    public void reloadCommand(Player player) {
        player.sendMessage(AFKUtil.c("&aAFK config.yml reloaded!"));
    }
    public void saveCommand(Player player) {
        configFile = new File(ToastedAFK.instance.getDataFolder(),"config.yml");
        file = YamlConfiguration.loadConfiguration(configFile);
        file.set("region.locations.world",tempLoc1.getWorld().getName());
        file.set("region.locations.loc1.x",tempLoc1.getX());
        file.set("region.locations.loc1.y",tempLoc1.getY());
        file.set("region.locations.loc1.z",tempLoc1.getZ());
        file.set("region.locations.loc2.x",tempLoc2.getX());
        file.set("region.locations.loc2.y",tempLoc2.getY());
        file.set("region.locations.loc2.z",tempLoc2.getZ());

        try {
            file.save(configFile);
            file.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        loc1=tempLoc1;
        loc2=tempLoc2;
        player.sendMessage(AFKUtil.c("&aNew region set!"));
    }
    public void wandCommand(Player player) {
        ItemStack wand;
        wand = new ItemStack(Material.STONE_AXE);
        ItemMeta wandMeta = wand.getItemMeta();
        wandMeta.setDisplayName(AFKUtil.c("&dAFK Wand"));
        wand.setItemMeta(wandMeta);

        player.getInventory().addItem(wand);
        player.sendMessage(AFKUtil.c("&aAFK Wand added!"));
    }
}
