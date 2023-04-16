package me.serbob.toastedafk.Commands;

import me.serbob.toastedafk.Functions.AFKCore;
import me.serbob.toastedafk.Managers.CommandsManager;
import me.serbob.toastedafk.Managers.ValuesManager;
import me.serbob.toastedafk.Templates.LoadingScreen;
import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.AFKUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

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
            sender.sendMessage(ChatColor.RED + "Usage: /tafk <wand/save/reload/item>");
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
                Player player = (Player) sender;
                if(player.getInventory().firstEmpty() != -1) {
                    wandCommand((Player) sender);
                }
                else {
                    sender.sendMessage(ChatColor.RED + "Your inventory is full!");
                }
            } else {
                noPermission(sender);
                return false;
            }
        } else if(args[0].equalsIgnoreCase("reload")) {
            if(sender.hasPermission("afk.reload")) {
                if(args.length<2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /tafk reload <safe/force> (force will reset player's timers, safe won't do that)");
                    return false;
                }
                if(args[1].equalsIgnoreCase("safe")) {
                    reloadCommand((Player) sender);
                } else if(args[1].equalsIgnoreCase("force")) {
                    reloadCommand((Player) sender);
                    playerStats.clear();
                    if(ToastedAFK.instance.getConfig().getBoolean("bossbar.show")) {
                        bossBar.removeAll();
                    }
                   // afkTimers.clear();
                }
                sender.sendMessage(AFKUtil.c("&aAFK config.yml reloaded!"));
            } else {
                noPermission(sender);
                return false;
            }
        } else if(args[0].equalsIgnoreCase("item")) {
          if(sender.hasPermission("afk.item")) {
              if(args.length<3) {
                  sender.sendMessage(ChatColor.RED+ "Usage: /tafk item <add/remove> <name to be created in config.yml>");
                  return false;
              }
              if(args[1].equalsIgnoreCase("add")) {
                  ItemStack item_to_be_added = ((Player) sender).getItemInHand();
                  if(ToastedAFK.instance.getConfig().getConfigurationSection("items").getKeys(false).contains(args[2])) {
                      sender.sendMessage(ChatColor.RED + "Item already exists in config.yml");
                      return false;
                  }
                  configFile = new File(ToastedAFK.instance.getDataFolder(),"config.yml");
                  file = YamlConfiguration.loadConfiguration(configFile);
                  file.set("items."+args[2],item_to_be_added);
                  try {
                      file.save(configFile);
                      file.load(configFile);
                  } catch (IOException | InvalidConfigurationException e) {
                      throw new RuntimeException(e);
                  }
                  reloadCommand((Player) sender);
                  sender.sendMessage(ChatColor.GREEN + "Item added!");
              } else if(args[1].equalsIgnoreCase("remove")) {
                  if((ToastedAFK.instance.getConfig().getConfigurationSection("items").getKeys(false).contains(args[2]))==false) {
                      sender.sendMessage(ChatColor.RED + "Item doesn't exist in config.yml");
                      return false;
                  }
                  configFile = new File(ToastedAFK.instance.getDataFolder(),"config.yml");
                  file = YamlConfiguration.loadConfiguration(configFile);
                  file.set("items."+args[2],null);
                  try {
                      file.save(configFile);
                      file.load(configFile);
                  } catch (IOException | InvalidConfigurationException e) {
                      throw new RuntimeException(e);
                  }
                  reloadCommand((Player) sender);
                  sender.sendMessage(ChatColor.GREEN + "Item removed!");
              }
          } else {
              noPermission(sender);
              return false;
          }
        } else if(args[0].equalsIgnoreCase("check")) {
            Player player = (Player) sender;
            sender.sendMessage(player.getItemInHand()+"");
            System.out.println(player.getItemInHand()+"");
        } else if(args[0].equalsIgnoreCase("list")) {
            sender.sendMessage(ChatColor.GREEN + "Total players AFK: "+playerStats.size());
            sender.sendMessage(ChatColor.GREEN + "List: "+playerStats);
        } else if(args[0].equalsIgnoreCase("bossbar")) {
            File configFile = new File(ToastedAFK.instance.getDataFolder(),"config.yml");
            YamlConfiguration file = YamlConfiguration.loadConfiguration(configFile);
            if(args.length<4) {
                sender.sendMessage(net.md_5.bungee.api.ChatColor.RED + "Usage: /tafk bossbar <add> <barColor> <barStyle>");
                return false;
            }
            BarColor barColor2 = BarColor.valueOf(args[2]);
            BarStyle barStyle2 = BarStyle.valueOf(args[3]);
            if(barColor2==null) {
                sender.sendMessage(net.md_5.bungee.api.ChatColor.RED + "Invalid bar color!");
                return false;
            }
            if(barStyle2==null) {
                sender.sendMessage(net.md_5.bungee.api.ChatColor.RED + "Invalid bar style!");
                return false;
            }
            bossBar.setColor(barColor2);
            bossBar.setStyle(barStyle2);
            file.set("bossbar.color",args[2]);
            file.set("bossbar.style",args[3]);
            try {
                file.save(configFile);
                file.load(configFile);
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }
            sender.sendMessage(ChatColor.GREEN + "New bossbar set!");
        }
        return true;
    }
    public void noPermission(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
    }
    public void reloadCommand(Player player) {
        try {

            // Create temporary file to hold new configuration
            Path tempPath = Files.createTempFile("config", ".yml");
            File tempFile = tempPath.toFile();
            tempFile.deleteOnExit();

            // Copy config.yml from plugin folder to temporary file
            InputStream input = ToastedAFK.instance.getResource("config.yml");
            Files.copy(input, tempPath, StandardCopyOption.REPLACE_EXISTING);

            // Load new configuration from temporary file
            FileConfiguration newConfig = new YamlConfiguration();
            newConfig.load(tempFile);

            // Set new configuration as the active configuration
            ToastedAFK.instance.reloadConfig();
            ToastedAFK.instance.getConfig().setDefaults(newConfig);
            ToastedAFK.instance.saveConfig();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        ValuesManager.loadConfigValues();
        CommandsManager.loadCommands();
        LoadingScreen.initializeLoadingScreen();
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
        wandMeta.setLore(Arrays.asList(AFKUtil.c("&cSet position 1 and 2 with this magic wand!")));
        wand.setItemMeta(wandMeta);

        player.getInventory().addItem(wand);
        player.sendMessage(AFKUtil.c("&aAFK Wand added!"));
    }
}
