package me.serbob.toastedafk.Functions;

import static me.serbob.toastedafk.Managers.ValuesManager.*;
import static me.serbob.toastedafk.Templates.CoreHelpers.*;
public class AFKCore {
    public static int globalSyncTime = DEFAULT_AFK_TIME+1;
    private static AFKCore instance;

    public static AFKCore getInstance() {
        if (instance == null)
            instance = new AFKCore();
        return instance;
    }

    public void addOrRemovePlayers() {
        --globalSyncTime;
        playerStats.keySet().forEach(player -> updatePlayer(player));
        if(globalSyncTime==0)
            globalSyncTime=DEFAULT_AFK_TIME+1;
    }
}