package it.fireentity.library.nms.packets;

import it.fireentity.library.interfaces.packets.IPacketPlayOutWindowItems;
import it.fireentity.library.inventories.Item;
import net.minecraft.server.v1_12_R1.PacketPlayOutWindowItems;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class PacketPlayOutWindowItems_v1_12_R1 extends Packet<PacketPlayOutWindowItems> implements IPacketPlayOutWindowItems {
    public PacketPlayOutWindowItems_v1_12_R1() {
        super(new PacketPlayOutWindowItems());
    }

    public void setWindowID(int windowID) {
        setField(getPacket(), "a", windowID);
    }

    public void setItems(TreeMap<Integer, Item> items, int inventorySize) {
        net.minecraft.server.v1_12_R1.ItemStack[] nmsItems = new net.minecraft.server.v1_12_R1.ItemStack[inventorySize];

        Iterator<Map.Entry<Integer, Item>> entries = items.entrySet().iterator();
        if(!entries.hasNext()) {
            return;
        }
        Map.Entry<Integer, Item> iteratedEntry = entries.next();
        for(int i = 0; i < nmsItems.length;i++) {
            if(iteratedEntry.getKey().equals(i)) {
                nmsItems[i] = CraftItemStack.asNMSCopy(iteratedEntry.getValue().getItemStack());
                if(entries.hasNext()) {
                    iteratedEntry = entries.next();
                }
            } else {
                nmsItems[i]= CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(Material.AIR));
            }
        }

        setField(getPacket(), "b", Arrays.asList(nmsItems));
    }

    public void send(Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(getPacket());
    }

}
