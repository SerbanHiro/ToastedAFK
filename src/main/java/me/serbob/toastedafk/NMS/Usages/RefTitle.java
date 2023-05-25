package me.serbob.toastedafk.NMS.Usages;

import me.serbob.toastedafk.Managers.VersionManager;
import me.serbob.toastedafk.NMS.Reflection;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

import static me.serbob.toastedafk.NMS.Reflection.getNMSClass;

public class RefTitle {
    public static void sendReflTitle(Player player, String title, String subtitle, int fadeIn, int showTime, int fadeOut) {
        //player.sendTitle(getTitle(player),getSubtitle(player), 0, 40, 0);
        Object playerHandle = Reflection.getPlayerHandle(player);
        Object playerConnection = Reflection.getPlayerConnection(playerHandle);

        Object titleObj = Reflection.createIChatBaseComponent(title);
        Object subtitleObj = Reflection.createIChatBaseComponent(subtitle);
        Object enumTitleAction = Reflection.getEnumConstant("PacketPlayOutTitle$EnumTitleAction", "TITLE");
        if(VersionManager.isVersion1_8OrAbove()) fadeIn=showTime=fadeOut=0;
        Object titlePacket = createTitlePacket(enumTitleAction, titleObj,fadeIn,showTime,fadeOut);
        Reflection.sendPacket(playerConnection, titlePacket);
        // Step 6: Construct the subtitle packet
        Object enumSubtitleAction = Reflection.getEnumConstant("PacketPlayOutTitle$EnumTitleAction", "SUBTITLE");
        Object subtitlePacket = createTitlePacket(enumSubtitleAction, subtitleObj,fadeIn,showTime,fadeOut);

        // Step 7: Send the subtitle packet to the player
        Reflection.sendPacket(playerConnection, subtitlePacket);
    }
    public static Object createTitlePacket(Object enumTitleAction, Object component, int fadeIn, int showTime, int fadeOut) {
        try {
            Class<?> titlePacketClass = getNMSClass("PacketPlayOutTitle");

            if (fadeIn == 0 && showTime == 0 && fadeOut == 0) {
                // For Spigot 1.8, create a title packet without timings
                Constructor<?> titlePacketConstructor = titlePacketClass.getConstructor(
                        getNMSClass("PacketPlayOutTitle$EnumTitleAction"), getNMSClass("IChatBaseComponent"));
                return titlePacketConstructor.newInstance(enumTitleAction, component);
            } else {
                // For Spigot 1.9 and 1.10, create a title packet with timings
                Constructor<?> titlePacketConstructor = titlePacketClass.getConstructor(
                        getNMSClass("PacketPlayOutTitle$EnumTitleAction"), getNMSClass("IChatBaseComponent"),
                        int.class, int.class, int.class);
                return titlePacketConstructor.newInstance(enumTitleAction, component, fadeIn, showTime, fadeOut);
            }
        } catch (Exception ex) {
            return enumTitleAction;
        }
    }
}