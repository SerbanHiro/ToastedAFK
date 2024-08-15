package me.serbob.toastedafk.NMS.Usages;

import me.serbob.toastedafk.NMS.Reflection;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class RefActionbar {
    private static final byte ACTION_BAR_MESSAGE_TYPE = 2;

    public static void sendActionBarPacket(Player player, String actionbarText) {
        Object playerHandle = Reflection.getPlayerHandle(player);
        Object playerConnection = Reflection.getPlayerConnection(playerHandle);
        Object actionbarPacket = createActionbarPacket(actionbarText);
        if (actionbarPacket != null) {
            Reflection.sendPacket(playerConnection, actionbarPacket);
        }
    }

    private static Object createActionbarPacket(String component) {
        try {
            Class<?> actionBarPacketClass = Reflection.getNMSClass("PacketPlayOutChat");
            Object chatComponent = Reflection.createIChatBaseComponent(component);

            Constructor<?> actionBarPacketConstructor = actionBarPacketClass.getConstructor(
                    Reflection.getNMSClass("IChatBaseComponent"), byte.class);
            return actionBarPacketConstructor.newInstance(chatComponent, ACTION_BAR_MESSAGE_TYPE);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}