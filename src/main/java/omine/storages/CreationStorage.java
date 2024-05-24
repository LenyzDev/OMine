package omine.storages;

import omine.managers.MineManager;
import omine.objects.Creation;
import omine.objects.RequiredItem;
import omine.objects.Skin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreationStorage {

    private MineManager mineManager;
    private HashMap<String, Creation> creationStorage = new HashMap<>();

    public CreationStorage(MineManager mineManager, FileConfiguration config){
        this.mineManager = mineManager;
        for(String key : config.getConfigurationSection("creations").getKeys(false)){
            List<RequiredItem> requiredItems = new ArrayList<>();
            for(String items : config.getConfigurationSection("creations." + key + ".items").getKeys(false)){
                if(items.equalsIgnoreCase("item1") &&
                items.equalsIgnoreCase("item2") &&
                items.equalsIgnoreCase("item3")){
                    continue;
                }
                requiredItems.add(new RequiredItem(
                        items,
                        config.getInt("creations." + key + ".items." + items)
                ));
            }
            String permission = config.getString("creations." + key + ".permission");
            List<String> commands = config.getStringList("creations." + key + ".commands");
            creationStorage.put(key, new Creation(requiredItems, permission, commands));
        }
    }

    public Creation getCreation(String key){
        return creationStorage.get(key);
    }

    public void addCreation(String key, Creation creation){
        creationStorage.put(key, creation);
    }

    public void removeCreation(String key){
        creationStorage.remove(key);
    }

    public boolean hasCreation(String key){
        return creationStorage.containsKey(key);
    }

    public HashMap<String, Creation> getCreationStorage(){
        return creationStorage;
    }

    public boolean makeCreation(String creationName, Player player){
        Creation creation = creationStorage.get(creationName);
        if(creation == null) return false;

        boolean hasItems = true;
        for(RequiredItem requiredItem : creation.getItem()){
            ItemStack item;
            switch (requiredItem.getItem()){
                case "item1":
                    item = mineManager.getItem1();
                    break;
                case "item2":
                    item = mineManager.getItem2();
                    break;
                case "item3":
                    item = mineManager.getItem3();
                    break;
                default:
                    return false;
            }
            if(!player.getInventory().containsAtLeast(item, requiredItem.getAmount())) {
                hasItems = false;
            }
        }
        if(!hasItems) return false;

        for(RequiredItem requiredItem : creation.getItem()){
            ItemStack item;
            switch (requiredItem.getItem()){
                case "item1":
                    item = mineManager.getItem1();
                    break;
                case "item2":
                    item = mineManager.getItem2();
                    break;
                case "item3":
                    item = mineManager.getItem3();
                    break;
                default:
                    return false;
            }
            removeItem(player.getInventory(), item, requiredItem.getAmount());
        }

        creation.getCommands().forEach(
                command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName()))
        );
        return true;
    }

    private void removeItem(Inventory inventory, ItemStack itemStack, int amount) {
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack slotItemStack = inventory.getItem(i);

            if (slotItemStack == null){
                continue;
            }

            if (slotItemStack.isSimilar(itemStack)) {
                if (slotItemStack.getAmount() > amount) {
                    slotItemStack.setAmount(slotItemStack.getAmount() - amount);
                    inventory.setItem(i, slotItemStack);
                    break;
                } else {
                    amount -= slotItemStack.getAmount();
                    inventory.setItem(i, null);
                }
            }

            if (amount == 0) {
                break;
            }
        }
    }
}
