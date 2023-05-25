package me.serbob.toastedafk.NMS;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Reflection {
    public static Object getPlayerHandle(Player player) {
        try {
            Method getHandleMethod = player.getClass().getMethod("getHandle");
            return getHandleMethod.invoke(player);
        } catch (Exception ex) {
            return null;
        }
    }

    public static Object getPlayerConnection(Object playerHandle) {
        try {
            Field connectionField = playerHandle.getClass().getField("playerConnection");
            return connectionField.get(playerHandle);
        } catch (Exception exception) {
            return playerHandle;
        }
    }

    public static Object createIChatBaseComponent(String text) {
        try {
            Class<?> chatSerializerClass = getNMSClass("IChatBaseComponent$ChatSerializer");
            Method aMethod = chatSerializerClass.getMethod("a", String.class);
            return aMethod.invoke(null, "{\"text\":\"" + text + "\"}");
        } catch (Exception ex) {
            return null;
        }
    }

    public static void sendPacket(Object playerConnection, Object packet) {
        try {
            Method sendPacketMethod = playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet"));
            sendPacketMethod.invoke(playerConnection, packet);
        } catch (Exception exception) {
            // Handle any exceptions
        }
    }

    public static Object getEnumConstant(String enumClassName, String constantName) {
        try {
            Class<?> enumClass = getNMSClass(enumClassName);
            return enumClass.getField(constantName).get(null);
        } catch (Exception ex) {
            return null;
        }
    }

    public static Class<?> getNMSClass(String className) throws ClassNotFoundException {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        String fullName = "net.minecraft.server." + version + "." + className;
        return Class.forName(fullName);
    }

}
