package omine.storages;

import omine.objects.Skin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class SkinStorage {

    private HashMap<String, Skin> skinStorage = new HashMap<>();

    public SkinStorage(FileConfiguration config){
        for(String skin : config.getConfigurationSection("skins").getKeys(false)){
            String name = config.getString("skins." + skin + ".name");
            double multiplier = config.getDouble("skins." + skin + ".multiplier");
            int customModelData = config.getInt("skins." + skin + ".custom-model-data");
            skinStorage.put(skin, new Skin(skin, name, multiplier, customModelData));
        }
    }

    public void addSkin(String skin, Skin skinReward) {
        skinStorage.put(skin, skinReward);
    }

    public void removeSkin(String skin) {
        skinStorage.remove(skin);
    }

    public boolean isSkin(String skin) {
        return skinStorage.containsKey(skin);
    }

    public Skin getSkin(String skin) {
        return skinStorage.get(skin);
    }

    public ItemStack getSkinItem(String skin) {
        return skinStorage.get(skin).getItem();
    }
}
