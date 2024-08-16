package me.serbob.toastedafk.tabcompleters;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SilentGiveTabCompleter implements TabCompleter {
    private final List<String> MATERIALS = Arrays.stream(Material.values())
            .map(material -> material.name().toLowerCase())
            .collect(Collectors.toList());

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return getOnlinePlayerNames(args[0]);
        } else if (args.length == 2) {
            return getFilteredMaterials(args[1]);
        } else if (args.length == 3) {
            return Collections.singletonList("<amount> (empty = 1)");
        }
        return new ArrayList<>();
    }

    private List<String> getOnlinePlayerNames(String prefix) {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(prefix.toLowerCase()))
                .collect(Collectors.toList());
    }

    private List<String> getFilteredMaterials(String prefix) {
        return MATERIALS.stream()
                .filter(material -> material.startsWith(prefix.toLowerCase()))
                .collect(Collectors.toList());
    }
}