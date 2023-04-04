package me.serbob.toastedafk.Managers;

import me.serbob.toastedafk.Events.DenyCommands;
import me.serbob.toastedafk.Events.InsideRegionEvents;
import me.serbob.toastedafk.Events.OPEvents;
import me.serbob.toastedafk.Events.WandEvents;
import me.serbob.toastedafk.ToastedAFK;

public class EventsManager {
    public static void loadEvents() {
        ToastedAFK.instance.getServer().getPluginManager().registerEvents(new OPEvents(),ToastedAFK.instance);
        ToastedAFK.instance.getServer().getPluginManager().registerEvents(new WandEvents(),ToastedAFK.instance);
        ToastedAFK.instance.getServer().getPluginManager().registerEvents(new InsideRegionEvents(),ToastedAFK.instance);
        ToastedAFK.instance.getServer().getPluginManager().registerEvents(new DenyCommands(),ToastedAFK.instance);
    }
}
