package it.fireentity.library.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import it.fireentity.library.AbstractPlugin;
import it.fireentity.library.inventories.PagesHandler;
import lombok.Getter;

@Getter
public abstract class GuiListener extends PacketAdapter {
    private final AbstractPlugin plugin;
    private final PagesHandler pagesHandler;

    public GuiListener(AbstractPlugin plugin, PagesHandler pagesHandler, PacketType packetType) {
        super(plugin, packetType);
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
        this.pagesHandler = pagesHandler;
        this.plugin = plugin;
    }
}
