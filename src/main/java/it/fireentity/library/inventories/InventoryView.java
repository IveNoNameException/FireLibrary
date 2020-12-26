package it.fireentity.library.inventories;

import it.fireentity.library.storage.GenericCache;

import java.util.Map;
import java.util.Optional;

public class InventoryView {
    private final GenericCache<Integer, Item> items = new GenericCache<>();

    public InventoryView(Map<Integer, Item> items) {
        addItems(items);
    }

    public void addItems(Map<Integer, Item> items) {
        for(Map.Entry<Integer, Item> item : items.entrySet()) {
            this.items.addValue(item.getKey(), item.getValue());
        }
    }

    public Optional<Item> getItem(Integer position) {
        return items.getValue(position);
    }
}
