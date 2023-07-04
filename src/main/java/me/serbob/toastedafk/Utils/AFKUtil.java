package me.serbob.toastedafk.Utils;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AFKUtil {
    public static String c(String preColor) {
        return ChatColor.translateAlternateColorCodes('&',format(preColor));
    }
    private static final Pattern HEX_PATTERN = Pattern.compile("&(#[A-Fa-f0-9]{6})");
    private static String format(String string) {
        Matcher matcher = HEX_PATTERN.matcher(string);
        while (matcher.find())
            string = string.replace(matcher.group(), "" + net.md_5.bungee.api.ChatColor.of(matcher.group().replace("&","")));
        return string;
    }
}
