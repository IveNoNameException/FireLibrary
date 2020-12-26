package it.fireentity.library.animations;

import it.fireentity.library.AbstractPlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Getter
public abstract class Animation {
    private final AbstractPlugin abstractPlugin;
    private final double degreesPerTick;
    private final int duration;
    private int taskID;
    private Location center;

    public Animation(AbstractPlugin abstractPlugin, double degreesPerTick, int duration) {
        this.duration = duration;
        this.abstractPlugin = abstractPlugin;
        this.degreesPerTick = degreesPerTick;
        Bukkit.getScheduler().runTaskLater(abstractPlugin, () -> onStop(center), duration);
    }

    public void start(Location center) {
        this.center = center;
        onStart(center);
        taskID = Bukkit.getScheduler().runTaskTimer(abstractPlugin, ()-> {
            this.play(center);
        }, 0, 1).getTaskId();
    }

    private void play(Location center) {
        onStart(center);
        onPlay(center);
    }

    protected abstract void onPlay(Location center);
    protected abstract void onStop(Location center);
    protected abstract void onStart(Location center);
}
