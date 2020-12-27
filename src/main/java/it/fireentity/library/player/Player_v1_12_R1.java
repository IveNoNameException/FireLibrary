package it.fireentity.library.player;

import it.fireentity.library.events.EventManager;
import it.fireentity.library.events.GuiOpenEvent;
import it.fireentity.library.inventories.GuiPage;
import it.fireentity.library.inventories.Inventory;
import it.fireentity.library.inventories.InventoryView;
import it.fireentity.library.inventories.Item;
import it.fireentity.library.nms.packets.PacketPlayOutCloseWindow_v1_12_R1;
import it.fireentity.library.nms.packets.PacketPlayOutWindowItems_v1_12_R1;
import it.fireentity.library.nms.packets.PacketPlayOutOpenWindow_v1_12_R1;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.inventory.ItemStack;

import java.util.TreeMap;

@RequiredArgsConstructor
public class Player_v1_12_R1 extends CustomPlayer {
    private final EventManager eventManager;

    public void sendInventoryContent(Inventory inventory) {
        TreeMap<Integer, Item> items = inventory.getItems(getPlayer());
        this.setInventoryView(new InventoryView(items));

        //Sending player inventory content
        final int INIT_OF_PACKET_INVENTORY = 9;
        ItemStack[] itemStacks = getPlayer().getInventory().getStorageContents();
        ItemStack[] stupidOrdered = new ItemStack[itemStacks.length];

        System.arraycopy(itemStacks, INIT_OF_PACKET_INVENTORY, stupidOrdered, 0, itemStacks.length - INIT_OF_PACKET_INVENTORY);
        System.arraycopy(itemStacks, 0, stupidOrdered, itemStacks.length - INIT_OF_PACKET_INVENTORY, INIT_OF_PACKET_INVENTORY);

        for(int i = 0; i < stupidOrdered.length; i++) {
            items.put(i+inventory.getSize(),new Item(stupidOrdered[i]));
        }

        PacketPlayOutWindowItems_v1_12_R1 packet = new PacketPlayOutWindowItems_v1_12_R1();
        packet.setItems(items, inventory.getSize() + this.getPlayer().getInventory().getStorageContents().length);
        packet.setWindowID(this.getWindowID());
        packet.send(this.getPlayer());
    }

    @Override
    public void closeInventory() {
        PacketPlayOutCloseWindow_v1_12_R1 packet = new PacketPlayOutCloseWindow_v1_12_R1();
        packet.setWindowID(getWindowID());
        packet.send(getPlayer());
        this.setInventoryView(null);
    }

    public void openInventory(GuiPage guiPage) {
        this.setWindowID(((CraftPlayer) getPlayer()).getHandle().nextContainerCounter());

        PacketPlayOutOpenWindow_v1_12_R1 packetOpenWindow = new PacketPlayOutOpenWindow_v1_12_R1();
        packetOpenWindow.setWindowID(getWindowID());
        packetOpenWindow.setTitle(guiPage.getInventory().getTitle());
        packetOpenWindow.setType(guiPage.getInventory().getInventoryType());
        packetOpenWindow.setSlots(guiPage.getInventory().getSize());
        packetOpenWindow.send(getPlayer());

        sendInventoryContent(guiPage.getInventory());

        eventManager.callEvent(new GuiOpenEvent(this, guiPage));
    }

    @Override
    public String getKey() {
        return getPlayer().getName();
    }
}
