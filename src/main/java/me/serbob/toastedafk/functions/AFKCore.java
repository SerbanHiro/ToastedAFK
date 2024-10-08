package me.serbob.toastedafk.functions;

import me.serbob.toastedafk.templates.CoreHelpers;

import static me.serbob.toastedafk.managers.ValuesManager.*;

public class AFKCore {
    private static final int GLOBAL_SYNC_TIME_RESET = DEFAULT_AFK_TIME + 1;
    public static int globalSyncTime = GLOBAL_SYNC_TIME_RESET;

    private static AFKCore instance;

    private AFKCore() {}

    public static AFKCore getInstance() {
        if (instance == null) {
            instance = new AFKCore();
        }
        return instance;
    }

    public void addOrRemovePlayers() {
        if (playerStats == null || playerStats.isEmpty()) {
            return;
        }

        globalSyncTime--;

        playerStats.keySet().forEach(CoreHelpers::updatePlayer);

        if (globalSyncTime == 0) {
            resetGlobalSyncTime();
        }
    }

    private void resetGlobalSyncTime() {
        globalSyncTime = GLOBAL_SYNC_TIME_RESET;
    }
}