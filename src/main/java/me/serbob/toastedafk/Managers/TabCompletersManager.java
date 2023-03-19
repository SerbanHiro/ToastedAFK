package me.serbob.toastedafk.Managers;

import me.serbob.toastedafk.Commands.AFKCommand;
import me.serbob.toastedafk.TabCompleters.AFKTabCompleter;
import me.serbob.toastedafk.TabCompleters.SilentGiveTabCompleter;
import me.serbob.toastedafk.ToastedAFK;

public class TabCompletersManager {
    public static void loadTabCompleters() {
        ToastedAFK.instance.getCommand("tafk").setTabCompleter(new AFKTabCompleter());
        ToastedAFK.instance.getCommand("silentgive").setTabCompleter(new SilentGiveTabCompleter());
    }
}
