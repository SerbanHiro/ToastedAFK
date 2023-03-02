package me.serbob.toastedafk.Managers;

import me.serbob.toastedafk.Functions.AFKCore;
import me.serbob.toastedafk.ToastedAFK;

public class AFKManager {
    public static void start() {
        ConsoleErrorManager.checkErrors();
        EventsManager.loadEvents();
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
