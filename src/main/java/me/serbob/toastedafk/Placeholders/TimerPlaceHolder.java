package me.serbob.toastedafk.Placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.serbob.toastedafk.Templates.ActionBar;
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
            return ActionBar.formatlPlaceholderActionBar(player.getPlayer());
        }
        return AFKUtil.c(ToastedAFK.instance.getConfig().getString("placeholder_not_in_afk_region"));
    }
}
