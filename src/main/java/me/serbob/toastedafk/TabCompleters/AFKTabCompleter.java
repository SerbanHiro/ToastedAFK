package me.serbob.toastedafk.TabCompleters;

import me.serbob.toastedafk.ToastedAFK;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class AFKTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> list = new ArrayList<>();
        if(args.length<2) {
            list.add("wand");
            list.add("save");
            list.add("reload");
            list.add("item");
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
        }
        return list;
    }
}
