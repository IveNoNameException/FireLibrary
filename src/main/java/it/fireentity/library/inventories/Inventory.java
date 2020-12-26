package it.fireentity.library.inventories;

import it.fireentity.library.events.GuiClickEvent;
import it.fireentity.library.inventories.section.Section;
import it.fireentity.library.storage.Cache;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.Optional;
import java.util.TreeMap;

public class Inventory {
    @Getter private final InventoryType inventoryType;
    @Getter private final boolean isEditable;
    @Getter private final String title;
    @Getter private final int size;
    private final Cache<String, Section> sections = new Cache<>();
    private final TreeMap<Integer, Item> items;

    public Inventory(boolean isEditable, String title, int size) {
        this.isEditable = isEditable;
        this.title = title;
        this.size = size;
        this.inventoryType = InventoryType.CHEST;
        this.items = new TreeMap<>();
    }

    public Inventory(boolean isEditable, String title, InventoryType inventoryType) {
        this.isEditable = isEditable;
        this.title = title;
        this.size = inventoryType.getDefaultSize();
        this.inventoryType = inventoryType;
        this.items = new TreeMap<>();
    }

    public TreeMap<Integer, Item> getStaticItems() {
        return items;
    }

    public void addSection(Section section) {
        this.sections.addValue(section);
    }

    public void addItems(TreeMap<Integer, Item> items) {
        this.items.putAll(items);
    }

    public void onPlayerClick(GuiClickEvent event) {
        if(!isEditable()) {
            event.setCancelled(true);
        }

        Optional<InventoryView> openedInventory = event.getPlayer().getInventoryView();
        if(!openedInventory.isPresent()) {
            return;
        }

        openedInventory.get().getItem(event.getSlot()).ifPresent(item -> item.onClick(event));
    }

    public void addItem(int position, Item item) {
        this.items.put(position, item);
    }

    public TreeMap<Integer, Item> getItems(Player player) {
        TreeMap<Integer, Item> items = new TreeMap<>(this.items);
        for(Section section : sections.getValues()) {
            section.getItems(player).ifPresent(items::putAll);
        }
        return items;
    }
}
