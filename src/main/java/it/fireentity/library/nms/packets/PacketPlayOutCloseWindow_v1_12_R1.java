package it.fireentity.library.nms.packets;

import it.fireentity.library.interfaces.packets.IPacketPlayOutCloseWindow;
import net.minecraft.server.v1_12_R1.PacketPlayOutCloseWindow;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PacketPlayOutCloseWindow_v1_12_R1 extends Packet<PacketPlayOutCloseWindow> implements IPacketPlayOutCloseWindow {
    public PacketPlayOutCloseWindow_v1_12_R1() {
        super(new PacketPlayOutCloseWindow());
    }

    public void setWindowID(int windowID) {
        setField(getPacket(),"a", windowID);
    }

    public Optional<Integer> getWindowID() {
        return getField(getPacket(), "a");
    }

    public void send(Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(getPacket());
    }
}
