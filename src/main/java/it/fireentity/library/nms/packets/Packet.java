package it.fireentity.library.nms.packets;

import lombok.AccessLevel;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.Optional;

public abstract class Packet<T> {
    @Getter(value = AccessLevel.PROTECTED) protected T packet;

    public Packet(T packet) {
        this.packet = packet;
    }

    protected void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target,value);
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected  <K> Optional<K> getField(Object target, String fieldName) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object object = field.get(target);
            field.setAccessible(false);
            return Optional.ofNullable((K)object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
