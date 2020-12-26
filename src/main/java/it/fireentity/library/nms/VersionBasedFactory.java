package it.fireentity.library.nms;

import it.fireentity.library.storage.GenericCache;
import org.bukkit.Server;

import java.util.Optional;
import java.util.function.Supplier;

public class VersionBasedFactory<T> {
    private final GenericCache<ServerVersion, Supplier<T>> cache = new GenericCache<>();

    public VersionBasedFactory<T> register(ServerVersion serverVersion, Supplier<T> t) {
        cache.addValue(serverVersion, t);
        return this;
    }

    public Optional<T> newInstance(Server server) {
        Optional<ServerVersion> serverVersion = ServerVersion.getCurrent(server);
        if (serverVersion.isPresent()) {
            return newInstance(serverVersion.get());
        }
        return Optional.empty();
    }

    public Optional<T> newInstance(ServerVersion serverVersion) {
        Optional<Supplier<T>> supplier = cache.getValue(serverVersion);
        return supplier.map(Supplier::get);
    }
}
