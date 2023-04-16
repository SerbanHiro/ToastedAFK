package me.serbob.toastedafk.Templates;

import me.serbob.toastedafk.Classes.PlayerStats;
import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.AFKUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static me.serbob.toastedafk.Managers.ValuesManager.*;
import static me.serbob.toastedafk.Templates.LoadingScreen.*;

public class CoreHelpers {
    public static boolean saveXpInsideRegion;
    public static boolean showXpBar;
    public static boolean bossBarShow;
    public static boolean actionBarShow;
    public static boolean titleScreenShow;
    public static String bossBarText;
    public static void readConfiguration() {
        saveXpInsideRegion = ToastedAFK.instance.getConfig().getBoolean("save_xp_inside_region");
        showXpBar = ToastedAFK.instance.getConfig().getBoolean("show_xp_bar");
        bossBarShow = ToastedAFK.instance.getConfig().getBoolean("bossbar.show");
        actionBarShow = ToastedAFK.instance.getConfig().getBoolean("actionbar.show");
        bossBarText = ToastedAFK.instance.getConfig().getString("bossbar.text");
        titleScreenShow = ToastedAFK.instance.getConfig().getBoolean("title_screen.show");
    }
    public static void updatePlayer(Player player) {
        if (actionBarShow) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                    ActionBar.formatNormalActionBar(player)));
        }
        if (bossBarShow) {
            if (bossBarText.equalsIgnoreCase("{timer}")) {
                bossBar.setTitle(ActionBar.formatNormalActionBar(player));
            } else {
                bossBar.setTitle(AFKUtil.c(bossBarText));
            }
        }
        if (showXpBar) {
            player.setLevel(playerStats.get(player).getAfkTimer());
        }
        if(titleScreenShow) {
            player.sendTitle(getTitle(player),getSubtitle(player), 0, 40, 0);
        }
    }
    public static void addPlayer(Player player) {
        int defaultAfkTime = getDefaultAfkTime(player);
        boolean playerXpEnabled = saveXpInsideRegion || showXpBar;
        playerStats.putIfAbsent(player, new PlayerStats(defaultAfkTime, defaultAfkTime, player.getLevel(),
                player.getExp(), playerXpEnabled));

        if (showXpBar) {
            player.setExp(1.0f);
        }
        if (bossBarShow) {
            bossBar.addPlayer(player);
        }
    }
    public static void removePlayer(Player player) {
        if (saveXpInsideRegion || showXpBar) {
            player.setLevel(playerStats.get(player).getLevelTimer());
            player.setExp(playerStats.get(player).getExpTimer());
        }
        if (playerStats.containsKey(player)) {
            playerStats.remove(player);
        }
        if (bossBarShow) {
            bossBar.removePlayer(player);
        }
    }
    private static int getDefaultAfkTime(Player player) {
        int defaultAfkTime = DEFAULT_AFK_TIME;
        for (String key : ToastedAFK.instance.getConfig().getConfigurationSection("afk_times").getKeys(false)) {
            if (player.hasPermission("afk.perm." + key)) {
                int perm_time = ToastedAFK.instance.getConfig().getInt("afk_times." + key);
                if (defaultAfkTime > perm_time) {
                    defaultAfkTime = perm_time;
                }
            }
        }
        return defaultAfkTime;
    }
    public static void executeCommands(Player player) {
        List<String> commands = ToastedAFK.instance.getConfig().getStringList("commands");
        commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName())));
    }
    public static void executeRandomCommands(Player player) {
        List<String> commands = ToastedAFK.instance.getConfig().getStringList("random_commands.list");
        int timesToExecute = ToastedAFK.instance.getConfig().getInt("random_commands.times");
        for (int i = 0; i < timesToExecute; i++) {
            Collections.shuffle(commands);
            if (!commands.isEmpty()) {
                int randomIndex = ThreadLocalRandom.current().nextInt(commands.size());
                String command = commands.get(randomIndex).replace("{player}", player.getName());
                ToastedAFK.instance.getServer().dispatchCommand(ToastedAFK.instance.getServer().getConsoleSender(), command);
            }
        }
    }
    public static void serverCrash() {
        Iterator<Map.Entry<Player, PlayerStats>> iterator = playerStats.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Player, PlayerStats> entry = iterator.next();
            Player player = entry.getKey();
            PlayerStats playerStatistic = entry.getValue();

            // Use playerStats to set the player's level and other stats
            int level = playerStatistic.getLevelTimer();
            float exp = playerStatistic.getExpTimer();
            player.setLevel(level);
            player.setExp(exp);

            // Remove the player's stats from the map
            iterator.remove();
        }
        if(bossBarShow) {
            bossBar.removeAll();
        }
    }
}
