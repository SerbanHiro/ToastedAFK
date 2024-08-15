package me.serbob.toastedafk.Templates;

import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.ChatUtil;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

import static me.serbob.toastedafk.Managers.ValuesManager.playerStats;
import static me.serbob.toastedafk.Templates.CoreHelpers.actionBarShowTimer;

public class ActionBar {
    private static final Configuration config = ToastedAFK.instance.getConfig();

    public static String formatNormalActionBar(Player player) {
        int time = playerStats.get(player).getAfkTimer();
        String timeString = formatTime(time);

        String beforeMsg = ChatUtil.c(config.getString("actionbar.messages.before_msg", "")
                .replace("{player}", player.getName())
                .replace("{loadingScreenBar}", LoadingScreen.getLoadingScreenBar(player))
                .replace("{loadingScreenPercentage}", LoadingScreen.getLoadingScreenPercentage(player)));

        return actionBarShowTimer ? beforeMsg + timeString : beforeMsg;
    }

    public static String formatlPlaceholderActionBar(Player player) {
        int time = playerStats.get(player).getAfkTimer();
        return formatTimeConcise(time);
    }

    private static String formatTime(int totalSeconds) {
        long days = TimeUnit.SECONDS.toDays(totalSeconds);
        long hours = TimeUnit.SECONDS.toHours(totalSeconds) % 24;
        long minutes = TimeUnit.SECONDS.toMinutes(totalSeconds) % 60;
        long seconds = totalSeconds % 60;

        StringBuilder result = new StringBuilder();
        appendTimeUnit(result, days, "day", "days");
        appendTimeUnit(result, hours, "hour", "hours");
        appendTimeUnit(result, minutes, "minute", "minutes");
        appendTimeUnit(result, seconds, "second", "seconds");

        return result.toString();
    }

    private static void appendTimeUnit(StringBuilder builder, long value, String singular, String plural) {
        if (value > 0) {
            builder.append(value).append(" ")
                    .append(ChatUtil.c(config.getString("actionbar.messages." + (value == 1 ? "sg_" : "pl_") + singular)))
                    .append(" ");
        }
    }

    private static String formatTimeConcise(int totalSeconds) {
        long days = TimeUnit.SECONDS.toDays(totalSeconds);
        long hours = TimeUnit.SECONDS.toHours(totalSeconds) % 24;
        long minutes = TimeUnit.SECONDS.toMinutes(totalSeconds) % 60;
        long seconds = totalSeconds % 60;

        StringBuilder result = new StringBuilder();
        if (days > 0) result.append(days).append("d ");
        if (hours > 0) result.append(hours).append("h ");
        if (minutes > 0) result.append(minutes).append("m ");
        if (seconds > 0 || result.length() == 0) result.append(seconds).append("s");

        return result.toString().trim();
    }
}