package me.serbob.toastedafk.NMS;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Reflection {
    private static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    public static Object getPlayerHandle(Player player) {
        try {
            Method getHandleMethod = player.getClass().getMethod("getHandle");
            return getHandleMethod.invoke(player);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Object getPlayerConnection(Object playerHandle) {
        try {
            Field connectionField = playerHandle.getClass().getField("playerConnection");
            return connectionField.get(playerHandle);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static Object createIChatBaseComponent(String text) {
        try {
            Class<?> chatSerializerClass = getNMSClass("IChatBaseComponent$ChatSerializer");
            Method aMethod = chatSerializerClass.getMethod("a", String.class);
            return aMethod.invoke(null, "{\"text\":\"" + text + "\"}");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void sendPacket(Object playerConnection, Object packet) {
        try {
            Method sendPacketMethod = playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet"));
            sendPacketMethod.invoke(playerConnection, packet);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static Object getEnumConstant(String enumClassName, String constantName) {
        try {
            Class<?> enumClass = getNMSClass(enumClassName);
            return enumClass.getField(constantName).get(null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Class<?> getNMSClass(String className) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + VERSION + "." + className);
    }
}