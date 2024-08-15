package me.serbob.toastedafk.TabCompleters;

import me.serbob.toastedafk.ToastedAFK;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AFKTabCompleter implements TabCompleter {
    private final List<String> MAIN_COMMANDS = Arrays.asList("wand", "save", "reload", "item", "check", "bossbar", "list");
    private final List<String> RELOAD_OPTIONS = Arrays.asList("safe", "force");
    private final List<String> ITEM_OPTIONS = Arrays.asList("add", "remove");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions = MAIN_COMMANDS;
        } else {
            String subCommand = args[0].toLowerCase();
            switch (subCommand) {
                case "reload":
                    if (args.length == 2) completions = RELOAD_OPTIONS;
                    break;
                case "item":
                    completions = handleItemTabComplete(args);
                    break;
                case "bossbar":
                    completions = handleBossbarTabComplete(args);
                    break;
            }
        }

        return filterStartsWith(completions, args[args.length - 1]);
    }

    private List<String> handleItemTabComplete(String[] args) {
        if (args.length == 2) {
            return ITEM_OPTIONS;
        } else if (args.length == 3) {
            return getConfigItems();
        }
        return new ArrayList<>();
    }

    private List<String> handleBossbarTabComplete(String[] args) {
        if (args.length == 2) {
            return Arrays.asList("add");
        } else if (args.length == 3) {
            return getBarColorNames();
        } else if (args.length == 4) {
            return getBarStyleNames();
        }
        return new ArrayList<>();
    }

    private List<String> getConfigItems() {
        return new ArrayList<>(ToastedAFK.instance.getConfig().getConfigurationSection("items").getKeys(false));
    }

    private List<String> getBarColorNames() {
        return Arrays.stream(BarColor.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    private List<String> getBarStyleNames() {
        return Arrays.stream(BarStyle.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    private List<String> filterStartsWith(List<String> list, String prefix) {
        String lowercasePrefix = prefix.toLowerCase();
        return list.stream()
                .filter(s -> s.toLowerCase().startsWith(lowercasePrefix))
                .collect(Collectors.toList());
    }
}