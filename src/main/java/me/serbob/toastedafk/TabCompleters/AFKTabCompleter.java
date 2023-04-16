package me.serbob.toastedafk.TabCompleters;

import me.serbob.toastedafk.ToastedAFK;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AFKTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> list = new ArrayList<>();
        if(args.length<2) {
            list.add("wand");
            list.add("save");
            list.add("reload");
            list.add("item");
            list.add("check");
            list.add("bossbar");
            list.add("list");
        } else if(args[0].equalsIgnoreCase("reload")) {
            list.add("safe");
            list.add("force");
        } else if(args[0].equalsIgnoreCase("item")) {
            if(args.length<3) {
                list.add("add");
                list.add("remove");
            } else if(args.length<4) {
                for(String key: ToastedAFK.instance.getConfig().getConfigurationSection("items").getKeys(false)) {
                    list.add(key);
                }
            }
        } else if(args[0].equalsIgnoreCase("bossbar")) {
            if(args.length<3) {
                list.add("add");
            } else if(args.length<4) {
                for(BarColor bC:BarColor.values()) {
                    list.add(bC.name());
                }
                String letters = args[2].toLowerCase();
                list = list.stream().filter(s -> s.toLowerCase().startsWith(letters)).collect(Collectors.toList());
            } else if(args.length<5) {
                for(BarStyle bS:BarStyle.values()) {
                    list.add(bS.name());
                }
                String letters = args[3].toLowerCase();
                list = list.stream().filter(s -> s.toLowerCase().startsWith(letters)).collect(Collectors.toList());
            }
        }
        return list;
    }
}
