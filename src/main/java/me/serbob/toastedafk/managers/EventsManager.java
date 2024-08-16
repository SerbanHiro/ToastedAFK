package me.serbob.toastedafk.managers;

import me.serbob.toastedafk.events.*;
import me.serbob.toastedafk.events.custom.Listener.RegionRelatedEventsHandler;
import me.serbob.toastedafk.events.custom.PlayerRegionHandler;
import me.serbob.toastedafk.ToastedAFK;

public class EventsManager {
    public static void loadEvents() {
        ToastedAFK.instance.getServer().getPluginManager().registerEvents(new RegionRelatedEventsHandler(), ToastedAFK.instance);
        ToastedAFK.instance.getServer().getPluginManager().registerEvents(new PlayerRegionHandler(), ToastedAFK.instance);
        ToastedAFK.instance.getServer().getPluginManager().registerEvents(new OPEvents(),ToastedAFK.instance);
        ToastedAFK.instance.getServer().getPluginManager().registerEvents(new WandEvents(),ToastedAFK.instance);
        ToastedAFK.instance.getServer().getPluginManager().registerEvents(new InsideRegionEvents(),ToastedAFK.instance);
        ToastedAFK.instance.getServer().getPluginManager().registerEvents(new DenyCommands(),ToastedAFK.instance);
        ToastedAFK.instance.getServer().getPluginManager().registerEvents(new XPDenyEvent(), ToastedAFK.instance);
    }
}
