package it.fireentity.library.nms.packets;

import it.fireentity.library.interfaces.packets.IPacketPlayInWindowClick;
import net.minecraft.server.v1_12_R1.InventoryClickType;
import net.minecraft.server.v1_12_R1.PacketPlayInWindowClick;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class PacketPlayInWindowClick_v1_12_R1 extends Packet<PacketPlayInWindowClick> implements IPacketPlayInWindowClick {

    public PacketPlayInWindowClick_v1_12_R1() {
        this(new PacketPlayInWindowClick());
    }

    public PacketPlayInWindowClick_v1_12_R1(PacketPlayInWindowClick packet) {
        super(packet);
    }

    public void setPacket(Object packet) {
        super.packet = (PacketPlayInWindowClick) packet;
    }

    public void setWindowID(int windowID) {
        setField(getPacket(), "a", windowID);
    }

    public void setSlot(int slot) {
        setField(getPacket(), "slot", slot);
    }

    public void setButton(int button) {
        setField(getPacket(), "button", button);
    }

    public void setAction(short action) {
        setField(getPacket(), "d", action);
    }

    public void setItemStack(ItemStack itemStack) {
        getField(itemStack, "handle").ifPresent(value -> setField(getPacket(), "item", value));
    }

    public void setInventoryClickType(int inventoryClickType) {
        setField(getPacket(), "shift", InventoryClickType.values()[inventoryClickType]);
    }

    @Override
    public Optional<Integer> getWindowID() {
        return getField(getPacket(), "a");
    }

    @Override
    public Optional<Integer> getSlot() {
        return getField(getPacket(), "slot");
    }

    @Override
    public Optional<Integer> getButton() {
        return getField(getPacket(), "button");
    }

    @Override
    public Optional<Short> getAction() {
        return getField(getPacket(), "d");
    }

    @Override
    public Optional<ItemStack> getItemStack() {
        return getField(getPacket(), "item").map(item -> CraftItemStack.asBukkitCopy((net.minecraft.server.v1_12_R1.ItemStack) item));
    }

    @Override
    public Optional<Integer> getInventoryClickType() {
        Optional<Object> field = getField(getPacket(), "shift");
        return field.map(o -> ((InventoryClickType) o).ordinal());
    }

    public void send(Player player) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(getPacket());
    }
}
