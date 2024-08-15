package me.serbob.toastedafk.NMS.Usages;

import me.serbob.toastedafk.Managers.VersionManager;
import me.serbob.toastedafk.NMS.Reflection;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class RefTitle {
    public static void sendReflTitle(Player player, String title, String subtitle, int fadeIn, int showTime, int fadeOut) {
        Object playerHandle = Reflection.getPlayerHandle(player);
        Object playerConnection = Reflection.getPlayerConnection(playerHandle);

        Object titleObj = Reflection.createIChatBaseComponent(title);
        Object subtitleObj = Reflection.createIChatBaseComponent(subtitle);

        if(VersionManager.isVersion1_8OrAbove()) {
            fadeIn = showTime = fadeOut = 0;
        }

        sendTitlePacket(playerConnection, "TITLE", titleObj, fadeIn, showTime, fadeOut);
        sendTitlePacket(playerConnection, "SUBTITLE", subtitleObj, fadeIn, showTime, fadeOut);
    }

    private static void sendTitlePacket(Object playerConnection, String action, Object component,
                                        int fadeIn, int showTime, int fadeOut) {
        Object enumTitleAction = Reflection.getEnumConstant("PacketPlayOutTitle$EnumTitleAction",
                action);
        Object titlePacket = createTitlePacket(enumTitleAction, component, fadeIn, showTime, fadeOut);
        if (titlePacket != null) {
            Reflection.sendPacket(playerConnection, titlePacket);
        }
    }

    private static Object createTitlePacket(Object enumTitleAction, Object component, int fadeIn, int showTime, int fadeOut) {
        try {
            Class<?> titlePacketClass = Reflection.getNMSClass("PacketPlayOutTitle");

            if (fadeIn == 0 && showTime == 0 && fadeOut == 0) {
                Constructor<?> titlePacketConstructor = titlePacketClass.getConstructor(
                        Reflection.getNMSClass("PacketPlayOutTitle$EnumTitleAction"),
                        Reflection.getNMSClass("IChatBaseComponent"));
                return titlePacketConstructor.newInstance(enumTitleAction, component);
            } else {
                Constructor<?> titlePacketConstructor = titlePacketClass.getConstructor(
                        Reflection.getNMSClass("PacketPlayOutTitle$EnumTitleAction"),
                        Reflection.getNMSClass("IChatBaseComponent"),
                        int.class, int.class, int.class);
                return titlePacketConstructor.newInstance(enumTitleAction, component, fadeIn, showTime, fadeOut);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}