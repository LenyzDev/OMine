package omine.events;

import omine.objects.BlockReward;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BreakRegenBlockEvent extends OMineEvents {

    private final Player player;
    private final BlockReward blockReward;
    private final Location location;

    public BreakRegenBlockEvent(Player player, BlockReward blockReward, Location location) {
        this.player = player;
        this.blockReward = blockReward;
        this.location = location;
    }

    public Player getPlayer() {
        return this.player;
    }

    public BlockReward getBlockReward() {
        return blockReward;
    }

    public Location getLocation() {
        return location;
    }
}
