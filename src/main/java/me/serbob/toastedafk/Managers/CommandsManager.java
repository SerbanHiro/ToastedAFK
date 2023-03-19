package me.serbob.toastedafk.Managers;

import me.serbob.toastedafk.Commands.AFKCommand;
import me.serbob.toastedafk.SilentCommands.SilentGiveCommand;
import me.serbob.toastedafk.ToastedAFK;

public class CommandsManager {
    public static void loadCommands() {
        ToastedAFK.instance.getCommand("tafk").setExecutor(new AFKCommand());
        ToastedAFK.instance.getCommand("silentgive").setExecutor(new SilentGiveCommand());
    }
}
