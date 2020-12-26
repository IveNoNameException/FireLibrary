package it.fireentity.library.nms;

import org.bukkit.Server;

import java.util.Optional;

public enum ServerVersion {

    v1_8_R3,

    v1_9_R2,

    v1_12_R1,

    v1_15_R1;

    private static final char PACKAGE_SEPARATOR = '.';

    public static String getCurrentName(Server server) {
        String name = server.getClass().getPackage().getName();
        return name.substring(name.lastIndexOf(PACKAGE_SEPARATOR) + 1);
    }

    public static Optional<ServerVersion> getCurrent(Server server) {
        try {
            return Optional.of(ServerVersion.valueOf(getCurrentName(server)));
        } catch (IllegalArgumentException ignored) {
        }

        return Optional.empty();
    }

}
