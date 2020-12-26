package it.fireentity.library.interfaces.packets;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public interface IPacketPlayOutOpenWindow {
    void setWindowID(int windowID);
    void setType(InventoryType type);
    void setTitle(String title);
    void setSlots(int slots);
    void setEntityID(int id);
    void send(Player player);
}
