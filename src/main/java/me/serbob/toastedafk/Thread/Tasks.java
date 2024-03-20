package me.serbob.toastedafk.Thread;

import me.serbob.toastedafk.ToastedAFK;

public class Tasks {
    public static void sync(Runnable runnable) {
        ToastedAFK.instance.getServer().getScheduler().runTask(ToastedAFK.instance, runnable);
    }
}
