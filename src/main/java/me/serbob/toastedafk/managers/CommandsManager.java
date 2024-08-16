package me.serbob.toastedafk.managers;

import me.serbob.toastedafk.commands.AFKCommand;
import me.serbob.toastedafk.commands.SilentGiveCommand;
import me.serbob.toastedafk.ToastedAFK;

public class CommandsManager {
    public static void loadCommands() {
        ToastedAFK.instance.getCommand("tafk").setExecutor(new AFKCommand());
        ToastedAFK.instance.getCommand("silentgive").setExecutor(new SilentGiveCommand());
    }
}
