package me.serbob.toastedafk.Managers;

import me.serbob.toastedafk.Events.*;
import me.serbob.toastedafk.Events.Custom.Listener.RegionRelatedEventsHandler;
import me.serbob.toastedafk.Events.Custom.PlayerRegionHandler;
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
