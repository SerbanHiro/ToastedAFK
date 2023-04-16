package me.serbob.toastedafk.Templates;

import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.AFKUtil;
import org.bukkit.entity.Player;

import java.util.Collections;

import static me.serbob.toastedafk.Managers.ValuesManager.playerStats;

public class LoadingScreen {
    public static int totalTiles;
    public static String symbol;
    public static void initializeLoadingScreen() {
        totalTiles = ToastedAFK.instance.getConfig().getInt("loading_screen.totalTiles");
        symbol = ToastedAFK.instance.getConfig().getString("loading_screen.symbol");
    }
    public static String getLoadingScreenBar(Player player) {
        int percentComplete = (playerStats.get(player).getDefaultAfkTime() -
                playerStats.get(player).getAfkTimer()) * 100 / playerStats.get(player).getDefaultAfkTime();
        int tilesToFill = percentComplete * totalTiles / 100;
        String incomingLoadingBar =
                AFKUtil.c(ToastedAFK.instance.getConfig().getString("loading_screen.loading_complete")) +
                        String.join("",
                                Collections.nCopies(tilesToFill, symbol))
                        + AFKUtil.c(ToastedAFK.instance.getConfig().getString("loading_screen.loading_color"))
                        + String.join("",
                        Collections.nCopies(totalTiles - tilesToFill, symbol));
        return incomingLoadingBar;
    }
    public static String getLoadingScreenPercentage(Player player) {
        int percentComplete = (playerStats.get(player).getDefaultAfkTime() -
                playerStats.get(player).getAfkTimer()) * 100 / playerStats.get(player).getDefaultAfkTime();
        return percentComplete+"";
    }
    public static String getTitle(Player player) {
        String title = AFKUtil.c(ToastedAFK.instance.getConfig().getString("title_screen.title"))
                .replace("{loadingScreenBar}",LoadingScreen.getLoadingScreenBar(player))
                .replace("{loadingScreenPercentage}",LoadingScreen.getLoadingScreenPercentage(player));
        return title;
    }
    public static String getSubtitle(Player player) {
        String percentage = AFKUtil.c(ToastedAFK.instance.getConfig().getString("title_screen.subtitle"))
                .replace("{loadingScreenBar}",LoadingScreen.getLoadingScreenBar(player))
                .replace("{loadingScreenPercentage}",LoadingScreen.getLoadingScreenPercentage(player));
        return percentage;
    }
}
