package me.serbob.toastedafk.Commands;

import me.serbob.toastedafk.NMS.Usages.RefActionbar;
import me.serbob.toastedafk.NMS.Usages.RefTitle;
import me.serbob.toastedafk.Templates.ActionBar;
import me.serbob.toastedafk.Utils.AFKUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import static me.serbob.toastedafk.Managers.VersionManager.isVersion1_12OrBelow;
import static me.serbob.toastedafk.Managers.VersionManager.isVersion1_8;
import static me.serbob.toastedafk.Templates.LoadingScreen.getSubtitle;
import static me.serbob.toastedafk.Templates.LoadingScreen.getTitle;

public class ALLVersionsCommandExecuter {
    public static void sendALLVersionsActionBar(Player player) {
        if(isVersion1_8()) {
            RefActionbar.sendActionBarPacket(player, ActionBar.formatNormalActionBar(player));
        } else {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(ActionBar.formatNormalActionBar(player)));
        }
    }
    public static void sendALLVersionsTitleScreen(Player player) {
        if(isVersion1_12OrBelow())
            RefTitle.sendReflTitle(player,getTitle(player),getSubtitle(player),0,40,0);
        else player.sendTitle(getTitle(player),getSubtitle(player),0,40,0);
    }
    public static void sendCUSTOMAllVersionsTitleScreen(Player player, String message) {
        player.sendTitle(AFKUtil.c(message),"",10,20,10);
    }
}
