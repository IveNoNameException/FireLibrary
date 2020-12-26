package it.fireentity.library.inventories.section;

import it.fireentity.library.interfaces.Cacheable;
import it.fireentity.library.inventories.Item;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class Section implements Cacheable<String> {
    private final String name;
    private List<Integer> positions = new ArrayList<>();

    public Section(String name) {
        this.name = name;
    }

    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }

    public void addPosition(Integer position) {
        positions.add(position);
    }

    public Collection<Integer> getPositions() {
        return positions;
    }

    public Optional<TreeMap<Integer, Item>> getItems(Player player) {
        return calculateContent(player);
    }

    public abstract Optional<TreeMap<Integer, Item>> calculateContent(Player player);

    @Override
    public String getKey() {
        return name;
    }
}
