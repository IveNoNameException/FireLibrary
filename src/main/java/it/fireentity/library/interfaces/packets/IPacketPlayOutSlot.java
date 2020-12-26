package it.fireentity.library.interfaces.packets;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public interface IPacketPlayOutSlot {
    void setWindowID(int windowID);
    void setItemStack(ItemStack itemStack);
    void setSlot(int slot);
    void send(Player player);

    Optional<Integer> getWindowID();
    Optional<Integer> getSlot();
    Optional<ItemStack> getItemStack();
}
