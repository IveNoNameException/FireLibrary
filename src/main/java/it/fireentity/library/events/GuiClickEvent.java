package it.fireentity.library.events;

import it.fireentity.library.nms.ServerVersion;
import it.fireentity.library.player.CustomPlayer;
import it.fireentity.library.interfaces.packets.IPacketPlayInWindowClick;
import it.fireentity.library.interfaces.packets.IPacketPlayOutSlot;
import it.fireentity.library.inventories.GuiPage;
import it.fireentity.library.nms.VersionBasedFactory;
import it.fireentity.library.nms.packets.PacketPlayOutSetSlot_v1_12_R1;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class GuiClickEvent extends GuiEvent implements Cancellable {
    private final VersionBasedFactory<IPacketPlayOutSlot> packetPlayOutSlot;
    private boolean isCancelled;
    @Getter
    private final IPacketPlayInWindowClick packetPlayInWindowClick;
    @Getter
    private final ServerVersion version;
    @Getter
    private final int slot;

    public GuiClickEvent(IPacketPlayInWindowClick packetPlayInWindowClick, ServerVersion version, GuiPage guiPage, CustomPlayer player, int slot) {
        super(player,guiPage);
        this.packetPlayInWindowClick = packetPlayInWindowClick;
        this.version = version;

        this.slot = convertSlot(slot);
        packetPlayOutSlot = new VersionBasedFactory<IPacketPlayOutSlot>().register(ServerVersion.v1_12_R1, PacketPlayOutSetSlot_v1_12_R1::new);
    }

    public final int convertSlot(int rawSlot) {
        int numInTop = getGuiPage().getInventory().getSize();
        if (rawSlot < numInTop) {
            return rawSlot;
        } else {
            int slot = rawSlot - numInTop;
            if (getGuiPage().getInventory().getInventoryType() == InventoryType.CRAFTING || getGuiPage().getInventory().getInventoryType() == InventoryType.CREATIVE) {
                if (slot < 4) {
                    return 39 - slot;
                }

                if (slot > 39) {
                    return slot;
                }

                slot -= 4;
            }

            if (slot >= 27) {
                slot -= 27;
            } else {
                slot += 9;
            }

            return slot;
        }
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
        Optional<Integer> windowID = getPacketPlayInWindowClick().getWindowID();

        if (!windowID.isPresent()) {
            return;
        }

        packetPlayOutSlot.newInstance(version).ifPresent(packet -> {
            packet.setSlot(-1);
            packet.setItemStack(new ItemStack(Material.AIR));
            packet.setWindowID(-1);
            packet.send(getPlayer().getPlayer());
        });

        getPlayer().sendInventoryContent(getGuiPage().getInventory());
    }
}
