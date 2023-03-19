package me.serbob.toastedafk.TabCompleters;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SilentGiveTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            for (Material material : Material.values()) {
                String name = material.name().toLowerCase();
                list.add(name);
            }
            list = list.stream()
                    .filter(letter -> letter.toLowerCase().startsWith(args[1]))
                    .collect(Collectors.toList());
        } else if(args.length == 3) {
            list.add("<amount> (empty = 1)");
        }
        return list;
    }
}
