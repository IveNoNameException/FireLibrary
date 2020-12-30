package it.fireentity.library.player;

import it.fireentity.library.inventories.InventoryView;
import it.fireentity.library.interfaces.Cacheable;
import it.fireentity.library.inventories.GuiPage;
import it.fireentity.library.inventories.Inventory;
import it.fireentity.library.inventories.PagesHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.Optional;

public abstract class CustomPlayer implements Cacheable<String> {
    @Getter @Setter private PagesHandler pagesHandler;
    @Getter @Setter(value = AccessLevel.PROTECTED) private Player player;
    @Getter @Setter private int windowID;
    @Setter(value = AccessLevel.PROTECTED) private InventoryView inventoryView;

    public abstract void openInventory(GuiPage guiPage);

    public abstract void sendInventoryContent(Inventory inventory);

    public abstract void closeInventory();

    public Optional<InventoryView> getInventoryView() {
        return Optional.ofNullable(inventoryView);
    }
}
