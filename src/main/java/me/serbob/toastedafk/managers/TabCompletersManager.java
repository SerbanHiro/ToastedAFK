package me.serbob.toastedafk.managers;

import me.serbob.toastedafk.tabcompleters.AFKTabCompleter;
import me.serbob.toastedafk.tabcompleters.SilentGiveTabCompleter;
import me.serbob.toastedafk.ToastedAFK;

public class TabCompletersManager {
    public static void loadTabCompleters() {
        ToastedAFK.instance.getCommand("tafk").setTabCompleter(new AFKTabCompleter());
        ToastedAFK.instance.getCommand("silentgive").setTabCompleter(new SilentGiveTabCompleter());
    }
}
