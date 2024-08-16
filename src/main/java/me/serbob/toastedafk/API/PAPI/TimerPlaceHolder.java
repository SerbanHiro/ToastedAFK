package me.serbob.toastedafk.API.PAPI;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.serbob.toastedafk.templates.ActionBar;
import me.serbob.toastedafk.ToastedAFK;
import me.serbob.toastedafk.utils.ChatUtil;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import static me.serbob.toastedafk.managers.ValuesManager.playerStats;

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
        return ChatUtil.c(ToastedAFK.instance.getConfig().getString("placeholder_not_in_afk_region"));
    }
}
