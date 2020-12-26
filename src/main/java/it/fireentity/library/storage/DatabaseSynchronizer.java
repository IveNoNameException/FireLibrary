package it.fireentity.library.storage;

import it.fireentity.library.AbstractPlugin;
import it.fireentity.library.interfaces.Cacheable;
import it.fireentity.library.interfaces.LoadableDatabaseUtility;

import java.util.ArrayList;
import java.util.List;

public class DatabaseSynchronizer<T extends Cacheable<K>, K> {

    protected final AbstractPlugin superCrates;
    private final LoadableDatabaseUtility<T, K> loadableDatabaseUtility;

    public DatabaseSynchronizer(AbstractPlugin superCrates, LoadableDatabaseUtility<T, K> loadableDatabaseUtility) {
        this.superCrates = superCrates;
        this.loadableDatabaseUtility = loadableDatabaseUtility;
    }

    public void synchronize(List<T> cachedObjects) {

        List<T> missingConfigObjects = new ArrayList<>();
        List<T> missingDatabaseObjects = new ArrayList<>();
        List<T> databaseObjects = loadableDatabaseUtility.load();

        //Check for differences between config and database
        for(T object : cachedObjects) {
            //Check if the message is into the config and not into the database
            if(cachedObjects.contains(object) && !databaseObjects.contains(object)) {
                missingDatabaseObjects.add(object);
            }
        }

        for(T object : databaseObjects) {
            //Check if the message is into the database and not into the config
            if(!cachedObjects.contains(object) && databaseObjects.contains(object)) {
                missingConfigObjects.add(object);
            }
        }

        //Adding missing messages into the database
        for(T object : missingDatabaseObjects) {
            loadableDatabaseUtility.insert(object);
        }

        //Removing messages names from the database
        for(T object : missingConfigObjects) {
            //Removing player active message
            loadableDatabaseUtility.remove(object);
        }
    }
}
