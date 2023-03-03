package me.serbob.toastedafk.Managers;

import me.serbob.toastedafk.Commands.AFKSaveCommand;
import me.serbob.toastedafk.Commands.AFKWandCommand;
import me.serbob.toastedafk.ToastedAFK;

public class CommandsManager {
    public static void loadCommands() {
        ToastedAFK.instance.getCommand("afkwand").setExecutor(new AFKWandCommand());
        ToastedAFK.instance.getCommand("afksave").setExecutor(new AFKSaveCommand());
    }
}
