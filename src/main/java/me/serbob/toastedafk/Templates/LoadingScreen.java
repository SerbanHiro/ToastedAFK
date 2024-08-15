package me.serbob.toastedafk.Templates;

import me.serbob.toastedafk.Classes.PlayerStats;
import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.ChatUtil;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.Collections;

import static me.serbob.toastedafk.Managers.ValuesManager.playerStats;

public class LoadingScreen {
    private static int totalTiles;
    private static String symbol;
    private static String loadingCompleteColor;
    private static String loadingColor;
    private static String titleFormat;
    private static String subtitleFormat;

    public static void initializeLoadingScreen() {
        Configuration config = ToastedAFK.instance.getConfig();
        totalTiles = config.getInt("loading_screen.totalTiles", 10);
        symbol = config.getString("loading_screen.symbol", "|");
        loadingCompleteColor = ChatUtil.c(config.getString("loading_screen.loading_complete", "&a"));
        loadingColor = ChatUtil.c(config.getString("loading_screen.loading_color", "&7"));
        titleFormat = config.getString("title_screen.title", "");
        subtitleFormat = config.getString("title_screen.subtitle", "");
    }

    public static String getLoadingScreenBar(Player player) {
        int percentComplete = calculatePercentComplete(player);
        int tilesToFill = percentComplete * totalTiles / 100;

        return loadingCompleteColor + String.join("", Collections.nCopies(tilesToFill, symbol)) +
                loadingColor + String.join("", Collections.nCopies(totalTiles - tilesToFill, symbol));
    }

    public static String getLoadingScreenPercentage(Player player) {
        return Integer.toString(calculatePercentComplete(player));
    }

    public static String getTitle(Player player) {
        return formatScreenText(titleFormat, player);
    }

    public static String getSubtitle(Player player) {
        return formatScreenText(subtitleFormat, player);
    }

    private static int calculatePercentComplete(Player player) {
        PlayerStats stats = playerStats.get(player);
        return (stats.getDefaultAfkTime() - stats.getAfkTimer()) * 100 / stats.getDefaultAfkTime();
    }

    private static String formatScreenText(String format, Player player) {
        return ChatUtil.c(format
                .replace("{loadingScreenBar}", getLoadingScreenBar(player))
                .replace("{loadingScreenPercentage}", getLoadingScreenPercentage(player))
                .replace("{timer}", ActionBar.formatNormalActionBar(player)));
    }
}