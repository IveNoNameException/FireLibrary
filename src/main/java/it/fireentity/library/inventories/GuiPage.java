package it.fireentity.library.inventories;

import it.fireentity.library.AbstractPlugin;
import it.fireentity.library.interfaces.Cacheable;
import lombok.Getter;

import java.util.Optional;

public class GuiPage implements Cacheable<String> {
    private final String name;
    private GuiPage nextGuiPage;
    private GuiPage previousGuiPage;
    @Getter private final Inventory inventory;

    public GuiPage(Inventory inventory, String name, AbstractPlugin abstractPlugin) {
        this.name = name;
        this.inventory = inventory;
        abstractPlugin.getPagesHandler().addPage(this);
    }

    public void setNextGuiPage(GuiPage guiPage) {
        this.nextGuiPage = guiPage;
    }

    public void setPreviousGuiPage(GuiPage guiPage) {
        this.previousGuiPage = guiPage;
    }

    public Optional<GuiPage> getNextGuiPage() {
        return Optional.ofNullable(nextGuiPage);
    }

    public Optional<GuiPage> getPreviousGuiPage() {
        return Optional.ofNullable(previousGuiPage);
    }

    public String getKey() {
        return name;
    }
}
