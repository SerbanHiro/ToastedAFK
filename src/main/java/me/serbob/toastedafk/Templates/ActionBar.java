package me.serbob.toastedafk.Templates;

import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.Utils.AFKUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import static me.serbob.toastedafk.Managers.ValuesManager.playerStats;

public class ActionBar {
    public static String formatNormalActionBar(Player player) {
        int time = playerStats.get(player).getAfkTimer();
        int days = time / (60 * 60 * 24);
        int hours = (time % (60 * 60 * 24)) / (60 * 60);
        int minutes = (time % (60 * 60)) / 60;
        int seconds = time % 60;
        String strDays = days==0 ? "" : days==1 ? days%7 +
                AFKUtil.c(ToastedAFK.instance.getConfig().getString("actionbar.messages.sg_day")): days%7 +
                AFKUtil.c(ToastedAFK.instance.getConfig().getString("actionbar.messages.pl_day"));
        String strHours = hours==0 ? "" : hours==1 ? hours +
                AFKUtil.c(ToastedAFK.instance.getConfig().getString("actionbar.messages.sg_hour")): hours+
                AFKUtil.c(ToastedAFK.instance.getConfig().getString("actionbar.messages.pl_hour"));
        String strMinutes = minutes==0 ? "" : minutes==1 ? minutes +
                AFKUtil.c(ToastedAFK.instance.getConfig().getString("actionbar.messages.sg_minute")): minutes +
                AFKUtil.c(ToastedAFK.instance.getConfig().getString("actionbar.messages.pl_minute"));
        String strSeconds = seconds==0 ?
                AFKUtil.c(ToastedAFK.instance.getConfig().getString("actionbar.messages.0_seconds")) : seconds==1 ? seconds +
                AFKUtil.c(ToastedAFK.instance.getConfig().getString("actionbar.messages.sg_second")) : seconds +
                AFKUtil.c(ToastedAFK.instance.getConfig().getString("actionbar.messages.pl_second"));
        String beforeMsg = AFKUtil.c(ToastedAFK.instance.getConfig().getString("actionbar.messages.before_msg")
                .replace("{player}",player.getName()));
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                beforeMsg + strDays + strHours + strMinutes + strSeconds));
        return beforeMsg + strDays + strHours + strMinutes + strSeconds;
        //try {
        //  player.stopAllSounds();
        //} catch (Exception ignored){}
    }
    public static String formatlPlaceholderActionBar(Player player) {
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
