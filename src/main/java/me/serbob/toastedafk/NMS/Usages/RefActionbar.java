package me.serbob.toastedafk.NMS.Usages;

import me.serbob.toastedafk.NMS.Reflection;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

import static me.serbob.toastedafk.NMS.Reflection.createIChatBaseComponent;
import static me.serbob.toastedafk.NMS.Reflection.getNMSClass;

public class RefActionbar {
    public static void sendActionBarPacket(Player player, String actionbarText) {
        Object playerHandle = Reflection.getPlayerHandle(player);
        Object playerConnection = Reflection.getPlayerConnection(playerHandle);
        Object actionbarPacket = RefActionbar.createActionbarPacket(actionbarText);
        Reflection.sendPacket(playerConnection, actionbarPacket);
    }
    public static Object createActionbarPacket(String component) {
        try {
            Class<?> actionBarPacketClass = getNMSClass("PacketPlayOutChat");

            Object chatComponent = createIChatBaseComponent(component);

            Constructor<?> actionBarPacketConstructor = actionBarPacketClass.getConstructor(
                    getNMSClass("IChatBaseComponent"), byte.class);
            byte actionBarMessageType = 2;
            return actionBarPacketConstructor.newInstance(chatComponent, actionBarMessageType);
        } catch (Exception ex) {
            return null;
        }
    }
}