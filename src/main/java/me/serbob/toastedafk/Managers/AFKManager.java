package me.serbob.toastedafk.Managers;

import me.serbob.toastedafk.Functions.AFKCore;
import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.Logger;
import me.serbob.toastedafk.Utils.RegionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

import static me.serbob.toastedafk.Managers.ValuesManager.*;
import static me.serbob.toastedafk.Managers.ValuesManager.afkTimers;
import static me.serbob.toastedafk.Utils.RegionUtils.playerInCubiod;

public class AFKManager {
    public static void start() {
        ConsoleErrorManager.checkErrors();
        EventsManager.loadEvents();
        CommandsManager.loadCommands();
        startScheduler();
    }
    public static void startScheduler() {
        ToastedAFK.instance.getServer().getScheduler().scheduleSyncRepeatingTask(ToastedAFK.instance, new Runnable() {
            public void run() {
                AFKCore.getInstance().addOrRemovePlayers();
                AFKCore.getInstance().regionCheck();
            }
        }, 0L, 20L);
    }
}
