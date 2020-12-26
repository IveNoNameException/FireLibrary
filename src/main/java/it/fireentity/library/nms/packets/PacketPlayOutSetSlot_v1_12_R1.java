package it.fireentity.library.nms.packets;

import it.fireentity.library.interfaces.packets.IPacketPlayOutSlot;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.PacketPlayOutSetSlot;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class PacketPlayOutSetSlot_v1_12_R1 extends Packet<PacketPlayOutSetSlot> implements IPacketPlayOutSlot {

    public PacketPlayOutSetSlot_v1_12_R1() {
        this(new PacketPlayOutSetSlot());
    }

    public PacketPlayOutSetSlot_v1_12_R1(PacketPlayOutSetSlot packet) {
        super(packet);
    }

    public void setWindowID(int windowID) {
        setField(getPacket(),"a",windowID);
    }

    public void setSlot(int slot) {
        setField(getPacket(), "b",slot);
    }

    @Override
    public void setItemStack(ItemStack itemStack) {
        if(itemStack == null) {
            itemStack = new ItemStack(Material.AIR);
        }
        getField(CraftItemStack.asCraftCopy(itemStack), "handle").ifPresent(value -> {
            net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = (net.minecraft.server.v1_12_R1.ItemStack) value;
            NBTTagCompound tag = nmsItemStack.getTag() != null ? nmsItemStack.getTag() : new NBTTagCompound();
            tag.setString("Fireentity293","Fireentity293");
            setField(getPacket(), "c", nmsItemStack);
        });
    }

    public Optional<Integer> getWindowID() {
        return getField(getPacket(), "a");
    }

    public Optional<Integer> getSlot() {
        return getField(getPacket(), "b");
    }

    public Optional<ItemStack> getItemStack() {
        Optional<net.minecraft.server.v1_12_R1.ItemStack> itemStack = getField(getPacket(), "c");
        return itemStack.map(CraftItemStack::asBukkitCopy);
    }

    public void send(Player player) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(getPacket());
    }
}
