package omine.objects;

import org.bukkit.Location;
import org.bukkit.Material;

public class BrokenBlock {

    private final Location location;
    private final Material material;
    private final long delay;
    private final long breakTime;

    public BrokenBlock(Location location, Material material, long delay, long breakTime) {
        this.location = location;
        this.material = material;
        this.delay = delay;
        this.breakTime = breakTime;
    }

    public Location getLocation() {
        return location;
    }

    public Material getMaterial() {
        return material;
    }

    public long getDelay() {
        return delay;
    }

    public long getBreakTime() {
        return breakTime;
    }
}
