package omine.events;

import org.bukkit.entity.Player;

public class PickaxeLevelUpEvent extends OMineEvents {

    private final Player player;
    private final int level;

    public PickaxeLevelUpEvent(Player player, int level) {
        this.player = player;
        this.level = level;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getLevel() {
        return level;
    }
}
