package me.serbob.toastedafk.Functions;

import me.serbob.toastedafk.Classes.PlayerStats;
import me.serbob.toastedafk.Managers.ValuesManager;
import me.serbob.toastedafk.Templates.ActionBar;
import me.serbob.toastedafk.Templates.CoreHelpers;
import me.serbob.toastedafk.Templates.ItemDistribution;
import me.serbob.toastedafk.Templates.LoadingScreen;
import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        if(playerStats == null) return;
        --globalSyncTime;
        for (Map.Entry<Player, PlayerStats> playerPlayerStatsEntry : playerStats.entrySet()) {
            updatePlayer(playerPlayerStatsEntry.getKey());
        }
        if(globalSyncTime==0)
            globalSyncTime=DEFAULT_AFK_TIME+1;
    }
}