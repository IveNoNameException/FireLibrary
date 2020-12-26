package it.fireentity.library.inventories;

import it.fireentity.library.events.GuiClickEvent;
import it.fireentity.library.interfaces.Action;
import it.fireentity.library.utils.SkullCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Optional;

public class Item implements Cloneable {

    private Action action;
    private ItemStack itemStack;

    public Item(ItemStack itemStack, Action action) {
        this.action = action;
        this.itemStack = itemStack;
    }

    public Item(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public Item(ItemStack itemStack, List<String> lore) {
        this(itemStack);
        if (lore != null) {
            setLore(lore);
        }
    }

    public Item(ItemStack itemStack, List<String> lore, short damage) {
        this(itemStack, lore);
        itemStack.setDurability(damage);
    }

    public Item(Material material, short data) {
        itemStack = new ItemStack(material, 1, data);
    }

    public Item(String url) {
        itemStack = SkullCreator.itemFromUrl(url);
    }

    public void onClick(GuiClickEvent event) {
        getAction().ifPresent(action -> action.onClick(event));
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setAmount(int amount) {
        itemStack.setAmount(amount);
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setLore(List<String> lore) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }

    public void setDisplayName(String displayName) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
    }

    public Optional<Action> getAction() {
        return Optional.ofNullable(action);
    }

    @Override
    public Item clone() {
        return new Item(itemStack.clone(), action);
    }
}
