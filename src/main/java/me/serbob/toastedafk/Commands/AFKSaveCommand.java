package me.serbob.toastedafk.Commands;

import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.AFKUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

import static me.serbob.toastedafk.Managers.ValuesManager.loc1;
import static me.serbob.toastedafk.Managers.ValuesManager.loc2;
import static me.serbob.toastedafk.Managers.ValuesManager.tempLoc1;
import static me.serbob.toastedafk.Managers.ValuesManager.tempLoc2;
import static me.serbob.toastedafk.ToastedAFK.*;

public class AFKSaveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You are not a player!");
            return false;
        }
        if(!sender.hasPermission("afksave.allow")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return false;
        }
        Player player = (Player) sender;

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
        return true;
    }
}
