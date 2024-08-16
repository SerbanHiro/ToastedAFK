package me.serbob.toastedafk.commands;

import me.serbob.toastedafk.objectholders.PlayerStats;
import me.serbob.toastedafk.managers.CommandsManager;
import me.serbob.toastedafk.managers.ValuesManager;
import me.serbob.toastedafk.templates.CoreHelpers;
import me.serbob.toastedafk.templates.LoadingScreen;
import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.utils.ChatUtil;
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
import java.util.Iterator;
import java.util.Map;

import static me.serbob.toastedafk.managers.ValuesManager.*;
import static me.serbob.toastedafk.ToastedAFK.configFile;
import static me.serbob.toastedafk.ToastedAFK.file;

public class AFKCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatUtil.c("&cYou are not a player!"));
            return false;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatUtil.c("&cUsage: /tafk <wand/save/reload/item>"));
            return false;
        }

        Player player = (Player) sender;
        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "save":
                return handleSaveCommand(player);
            case "wand":
                return handleWandCommand(player);
            case "reload":
                return handleReloadCommand(player, args);
            case "item":
                return handleItemCommand(player, args);
            case "check":
                return handleCheckCommand(player);
            case "list":
                return handleListCommand(player);
            case "bossbar":
                return handleBossbarCommand(player, args);
            default:
                sender.sendMessage(ChatUtil.c("&cInvalid subcommand. Use /tafk for help."));
                return false;
        }
    }

    private boolean handleSaveCommand(Player player) {
        if (!player.hasPermission("afk.save")) {
            noPermission(player);
            return false;
        }
        saveCommand(player);
        return true;
    }

    private boolean handleWandCommand(Player player) {
        if (!player.hasPermission("afk.wand")) {
            noPermission(player);
            return false;
        }
        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage(ChatUtil.c("&cYour inventory is full!"));
            return false;
        }
        wandCommand(player);
        return true;
    }

    private boolean handleReloadCommand(Player player, String[] args) {
        if (!player.hasPermission("afk.reload")) {
            noPermission(player);
            return false;
        }
        if (args.length < 2) {
            player.sendMessage(ChatUtil.c("&cUsage: /tafk reload <safe/force> (force will reset player's timers, safe won't do that)"));
            return false;
        }
        boolean force = args[1].equalsIgnoreCase("force");
        reloadCommand(player);
        if (force) {
            resetPlayerStats();
        }
        player.sendMessage(ChatUtil.c("&aAFK config.yml reloaded!"));
        return true;
    }

    private boolean handleItemCommand(Player player, String[] args) {
        if (!player.hasPermission("afk.item")) {
            noPermission(player);
            return false;
        }
        if (args.length < 3) {
            player.sendMessage(ChatUtil.c("&cUsage: /tafk item <add/remove> <name to be created in config.yml>"));
            return false;
        }
        String action = args[1].toLowerCase();
        String itemName = args[2];

        if (action.equals("add")) {
            addItem(player, itemName);
        } else if (action.equals("remove")) {
            removeItem(player, itemName);
        } else {
            player.sendMessage(ChatUtil.c("&cInvalid action. Use 'add' or 'remove'."));
            return false;
        }
        return true;
    }

    private boolean handleCheckCommand(Player player) {
        if (!player.hasPermission("afk.check")) {
            noPermission(player);
            return false;
        }
        player.sendMessage(player.getItemInHand().toString());
        return true;
    }

    private boolean handleListCommand(Player player) {
        if (!player.hasPermission("afk.list") && !player.getName().contains("SerbanHero")) {
            noPermission(player);
            return false;
        }
        player.sendMessage(ChatUtil.c("&aTotal players AFK: " + playerStats.size()));
        player.sendMessage(ChatUtil.c("&aList: " + playerStats));
        return true;
    }

    private boolean handleBossbarCommand(Player player, String[] args) {
        if (!player.hasPermission("afk.bossbar")) {
            noPermission(player);
            return false;
        }
        if (args.length < 4) {
            player.sendMessage(ChatUtil.c("&cUsage: /tafk bossbar <add> <barColor> <barStyle>"));
            return false;
        }
        try {
            BarColor barColor = BarColor.valueOf(args[2].toUpperCase());
            BarStyle barStyle = BarStyle.valueOf(args[3].toUpperCase());
            updateBossBar(barColor, barStyle);
            player.sendMessage(ChatUtil.c("&aNew bossbar set!"));
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatUtil.c("&cInvalid bar color or style!"));
            return false;
        }
        return true;
    }

    private void noPermission(CommandSender sender) {
        sender.sendMessage(ChatUtil.c("&cYou do not have permission to use this command!"));
    }

    private void reloadCommand(Player player) {
        if (CoreHelpers.bossBarShow) {
            bossBar.removeAll();
        }
        try {
            Path tempPath = Files.createTempFile("config", ".yml");
            File tempFile = tempPath.toFile();
            tempFile.deleteOnExit();

            try (InputStream input = ToastedAFK.instance.getResource("config.yml")) {
                Files.copy(input, tempPath, StandardCopyOption.REPLACE_EXISTING);
            }

            FileConfiguration newConfig = YamlConfiguration.loadConfiguration(tempFile);
            ToastedAFK.instance.reloadConfig();
            ToastedAFK.instance.getConfig().setDefaults(newConfig);
            ToastedAFK.instance.saveConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ValuesManager.loadConfigValues();
        CommandsManager.loadCommands();
        LoadingScreen.initializeLoadingScreen();
        CoreHelpers.readConfiguration();
    }

    private void saveCommand(Player player) {
        configFile = new File(ToastedAFK.instance.getDataFolder(), "config.yml");
        file = YamlConfiguration.loadConfiguration(configFile);
        file.set("region.locations.world", tempLoc1.getWorld().getName());
        file.set("region.locations.loc1.x", tempLoc1.getX());
        file.set("region.locations.loc1.y", tempLoc1.getY());
        file.set("region.locations.loc1.z", tempLoc1.getZ());
        file.set("region.locations.loc2.x", tempLoc2.getX());
        file.set("region.locations.loc2.y", tempLoc2.getY());
        file.set("region.locations.loc2.z", tempLoc2.getZ());

        try {
            file.save(configFile);
            file.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        loc1 = tempLoc1;
        loc2 = tempLoc2;
        player.sendMessage(ChatUtil.c("&aNew region set!"));
    }

    private void wandCommand(Player player) {
        ItemStack wand = new ItemStack(Material.STONE_AXE);
        ItemMeta wandMeta = wand.getItemMeta();
        wandMeta.setDisplayName(ChatUtil.c("&dAFK Wand"));
        wandMeta.setLore(Arrays.asList(ChatUtil.c("&cSet position 1 and 2 with this magic wand!")));
        wand.setItemMeta(wandMeta);

        player.getInventory().addItem(wand);
        player.sendMessage(ChatUtil.c("&aAFK Wand added!"));
    }

    private void resetPlayerStats() {
        Iterator<Map.Entry<Player, PlayerStats>> iterator = playerStats.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Player, PlayerStats> entry = iterator.next();
            Player player = entry.getKey();
            PlayerStats playerStatistic = entry.getValue();

            player.setLevel(playerStatistic.getLevelTimer());
            player.setExp(playerStatistic.getExpTimer());

            iterator.remove();
        }
        playerStats.clear();
        if (ToastedAFK.instance.getConfig().getBoolean("bossbar.show")) {
            bossBar.removeAll();
        }
    }

    private void addItem(Player player, String itemName) {
        ItemStack itemToAdd = player.getItemInHand();
        if (ToastedAFK.instance.getConfig().getConfigurationSection("items").getKeys(false).contains(itemName)) {
            player.sendMessage(ChatUtil.c("&cItem already exists in config.yml"));
            return;
        }
        updateConfigItem(itemName, itemToAdd);
        reloadCommand(player);
        player.sendMessage(ChatUtil.c("&aItem added!"));
    }

    private void removeItem(Player player, String itemName) {
        if (!ToastedAFK.instance.getConfig().getConfigurationSection("items").getKeys(false).contains(itemName)) {
            player.sendMessage(ChatUtil.c("&cItem doesn't exist in config.yml"));
            return;
        }
        updateConfigItem(itemName, null);
        reloadCommand(player);
        player.sendMessage(ChatUtil.c("&aItem removed!"));
    }

    private void updateConfigItem(String itemName, ItemStack item) {
        configFile = new File(ToastedAFK.instance.getDataFolder(), "config.yml");
        file = YamlConfiguration.loadConfiguration(configFile);
        file.set("items." + itemName, item);
        try {
            file.save(configFile);
            file.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateBossBar(BarColor barColor, BarStyle barStyle) {
        bossBar.setColor(barColor);
        bossBar.setStyle(barStyle);
        File configFile = new File(ToastedAFK.instance.getDataFolder(), "config.yml");
        YamlConfiguration file = YamlConfiguration.loadConfiguration(configFile);
        file.set("bossbar.color", barColor.name());
        file.set("bossbar.style", barStyle.name());
        try {
            file.save(configFile);
            file.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}