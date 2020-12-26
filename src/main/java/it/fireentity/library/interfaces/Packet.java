package it.fireentity.library.interfaces;

import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Optional;

public interface Packet {
    default void setField(Object target, String fieldName, Object value) {
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target,value);
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    default <T> Optional<T> getField(Object target, String fieldName) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object object = field.get(target);
            field.setAccessible(false);
            return Optional.ofNullable((T)object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    void send(Player player);

}
