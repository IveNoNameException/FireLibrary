package it.fireentity.library.interfaces.packets;

import java.util.Optional;

public interface IPacketPlayOutCloseWindow {
    void setWindowID(int windowID);
    Optional<Integer> getWindowID();
}
