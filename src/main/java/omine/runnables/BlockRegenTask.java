package omine.runnables;

import com.github.fierioziy.particlenativeapi.api.ParticleNativeAPI;
import omine.OMine;
import omine.objects.BrokenBlock;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BlockRegenTask implements Runnable {

    private HashMap<Location, BrokenBlock> brokenBlocks = new HashMap<>();
    private final ParticleNativeAPI particleNativeAPI;

    public BlockRegenTask(ParticleNativeAPI particleNativeAPI) {
        this.particleNativeAPI = particleNativeAPI;
    }

    @Override
    public void run() {
        updateBrokenBlocks();
    }

    public void addBrokenBlock(Location location, BrokenBlock brokenBlock) {
        brokenBlocks.put(location, brokenBlock);
    }

    public void removeBrokenBlock(Location location) {
        brokenBlocks.remove(location);
    }

    public boolean isBrokenBlock(Location location) {
        return brokenBlocks.containsKey(location);
    }

    public BrokenBlock get(Location location) {
        return brokenBlocks.get(location);
    }

    public void updateBrokenBlocks() {
        for (Iterator<Map.Entry<Location, BrokenBlock>> it = brokenBlocks.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Location, BrokenBlock> entry = it.next();
            Location location = entry.getKey();
            BrokenBlock brokenBlock = entry.getValue();
            if (brokenBlock.getBreakTime() + brokenBlock.getDelay() <= System.currentTimeMillis()) {
                location.getBlock().setType(brokenBlock.getMaterial());
                particleNativeAPI.LIST_1_13.HAPPY_VILLAGER.packet(true, getCenter(location), 0.5D, 0.5D, 0.5D, 15).sendTo(location.getNearbyPlayers(7));
                it.remove();
            }
        }
    }

    public HashMap<Location, BrokenBlock> getBrokenBlocks() {
        return brokenBlocks;
    }

    public Location getCenter(Location location) {
        return new Location(location.getWorld(), location.getBlockX() + 0.5D, location.getBlockY() + 0.5D, location.getBlockZ() + 0.5D);
    }
}
