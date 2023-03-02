package me.serbob.toastedafk.Managers;

import me.serbob.toastedafk.Events.OPJoined;
import me.serbob.toastedafk.ToastedAFK;

public class EventsManager {
    public static void loadEvents() {
        ToastedAFK.instance.getServer().getPluginManager().registerEvents(new OPJoined(),ToastedAFK.instance);
    }
}
