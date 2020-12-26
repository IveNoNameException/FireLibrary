package it.fireentity.library.interfaces.packets;

import it.fireentity.library.inventories.Item;
import org.bukkit.entity.Player;

import java.util.TreeMap;

public interface IPacketPlayOutWindowItems {
    void setWindowID(int windowID);

    void setItems(TreeMap<Integer, Item> items, int inventorySize);
    void send(Player player);


}
