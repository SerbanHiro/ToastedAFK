package me.serbob.toastedafk.Placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.AFKUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.serbob.toastedafk.Managers.ValuesManager.playerStats;

public class TimerPlaceHolder extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "afktimer";
    }

    @Override
    public String getAuthor() {
        return "serbob";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if(playerStats.containsKey(player.getPlayer())) {
            return formatPlaceholder(player.getPlayer());
        }
        return AFKUtil.c(ToastedAFK.instance.getConfig().getString("placeholder_not_in_afk_region"));
    }

    public String formatPlaceholder(Player player) {
        int time = playerStats.get(player).getAfkTimer();
        int days = time / (60 * 60 * 24);
        int hours = (time % (60 * 60 * 24)) / (60 * 60);
        int minutes = (time % (60 * 60)) / 60;
        int seconds = time % 60;
        String strDays = days==0 ? "" : days%7 + "d ";
        String strHours = hours==0 ? "" : hours + "h ";
        String strMinutes = minutes==0 ? "" : minutes + "m ";
        String strSeconds = seconds==0 ? "" : seconds + "s";
        String beforeMsg = AFKUtil.c("Time left: ");
        //player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
              //  beforeMsg + strDays + strHours + strMinutes + strSeconds));
        //try {
        //  player.stopAllSounds();
        //} catch (Exception ignored){}
        return strDays + strHours + strMinutes + strSeconds;
    }
}
