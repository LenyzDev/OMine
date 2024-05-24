package omine.storages;

import omine.objects.BlockReward;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class BlockStorage {

    private HashMap<String, BlockReward> blockStorage = new HashMap<>();

    public BlockStorage(FileConfiguration config) {
        for(String block : config.getConfigurationSection("blocks").getKeys(false)) {

            ConfigurationSection configBlocks = config.getConfigurationSection("blocks");
            Material material = Material.matchMaterial(block);

            String name = configBlocks.getString(block + ".name");
            String permission = configBlocks.getString(block + ".permission");
            String errorMessage = configBlocks.getString(block + ".error-message");
            int delay = configBlocks.getInt(block + ".delay");
            double experience = configBlocks.getDouble(block + ".experience");
            double money = configBlocks.getDouble(block + ".money");
            double economy = configBlocks.getDouble(block + ".economy");
            boolean item1 = configBlocks.getBoolean(block + ".item1", false);
            boolean item2 = configBlocks.getBoolean(block + ".item2", false);
            boolean item3 = configBlocks.getBoolean(block + ".item3", false);

            blockStorage.put(material.toString(), new BlockReward(name, permission, errorMessage, delay, experience, money, economy, item1, item2, item3));
        }
    }

    public void addBlock(String material, BlockReward blockReward) {
        blockStorage.put(material, blockReward);
    }

    public void removeBlock(String material) {
        blockStorage.remove(material);
    }

    public boolean isBlock(Material material) {
        return blockStorage.containsKey(material.toString());
    }

    public boolean isBlock(String material) {
        return blockStorage.containsKey(material);
    }

    public BlockReward getBlock(String material) {
        return blockStorage.get(material);
    }

}
