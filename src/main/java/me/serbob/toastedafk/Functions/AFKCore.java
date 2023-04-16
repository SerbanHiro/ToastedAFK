package me.serbob.toastedafk.Functions;

import me.serbob.toastedafk.Classes.PlayerStats;
import me.serbob.toastedafk.Templates.ActionBar;
import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.List;
import java.util.Set;

import static me.serbob.toastedafk.Managers.ValuesManager.*;
import static me.serbob.toastedafk.Templates.CoreHelpers.*;

public class AFKCore {
    private static AFKCore instance;
    public static AFKCore getInstance() {
        if (instance == null)
            instance = new AFKCore();
        return instance;
    }

    public void regionCheck() {
        List<String> commands = ToastedAFK.instance.getConfig().getStringList("commands");
        playerStats.forEach((player, playerStatistics) -> {
            int timeLeft = playerStatistics.getAfkTimer();
            if (timeLeft-- <= 0) {
                commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName())));
                playerStatistics.setAfkTimer(playerStatistics.getDefaultAfkTime());
            } else {
                playerStatistics.setAfkTimer(timeLeft);
            }
        });
    }

    public void addOrRemovePlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (RegionUtils.playerInCubiod(player.getLocation(), loc1, loc2)) {
                if(!playerStats.containsKey(player)) {
                    addPlayer(player);
                }
                updatePlayer(player);
            } else {
                removePlayer(player);
            }
        }
    }
}