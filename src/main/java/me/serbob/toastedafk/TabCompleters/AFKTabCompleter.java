package me.serbob.toastedafk.TabCompleters;

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
        }
        return list;
    }
}
