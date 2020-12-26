package it.fireentity.library.nms.packets;

import it.fireentity.library.interfaces.packets.IPacketPlayOutOpenWindow;
import net.minecraft.server.v1_12_R1.ChatComponentText;
import net.minecraft.server.v1_12_R1.PacketPlayOutOpenWindow;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class PacketPlayOutOpenWindow_v1_12_R1 extends Packet<PacketPlayOutOpenWindow> implements IPacketPlayOutOpenWindow {

    public PacketPlayOutOpenWindow_v1_12_R1() {
        super(new PacketPlayOutOpenWindow());
    }

    public void setWindowID(int windowID) {
        setField(getPacket(), "a", windowID);
    }

    public void setType(InventoryType type) {
        switch (type) {
            case WORKBENCH:
                setField(getPacket(), "b", "minecraft:crafting_table");
                break;
            case FURNACE:
                setField(getPacket(), "b", "minecraft:furnace");
                break;
            case DISPENSER:
                setField(getPacket(), "b", "minecraft:dispenser");
                break;
            case ENCHANTING:
                setField(getPacket(), "b", "minecraft:enchanting_table");
                break;
            case BREWING:
                setField(getPacket(), "b", "minecraft:brewing_stand");
                break;
            case BEACON:
                setField(getPacket(), "b", "minecraft:beacon");
                break;
            case ANVIL:
                setField(getPacket(), "b", "minecraft:anvil");
                break;
            case HOPPER:
                setField(getPacket(), "b", "minecraft:hopper");
                break;
            case DROPPER:
                setField(getPacket(), "b", "minecraft:dropper");
                break;
            case SHULKER_BOX:
                setField(getPacket(), "b", "minecraft:shulker_box");
                break;
            default:
                setField(getPacket(), "b", "minecraft:chest");

        }
    }

    public void setTitle(String title) {
        setField(getPacket(), "c", new ChatComponentText(title));
    }

    public void setSlots(int slots) {
        setField(getPacket(), "d", slots);
    }

    public void setEntityID(int id) {
        setField(getPacket(), "e", id);
    }

    public void send(Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(getPacket());
    }
}
