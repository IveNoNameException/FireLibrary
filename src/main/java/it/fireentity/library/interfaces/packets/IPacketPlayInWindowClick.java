package it.fireentity.library.interfaces.packets;

import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public interface IPacketPlayInWindowClick {
    void setWindowID(int windowID);
    void setSlot(int slot);
    void setButton(int button);
    void setAction(short action);
    void setItemStack(ItemStack itemStack);
    void setInventoryClickType(int inventoryClickType);
    void setPacket(Object packet);

    Optional<Integer> getWindowID();
    Optional<Integer> getSlot();
    Optional<Integer> getButton();
    Optional<Short> getAction();
    Optional<ItemStack> getItemStack();
    Optional<Integer> getInventoryClickType();
}
