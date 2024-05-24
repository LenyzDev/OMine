package omine.objects;

import com.lib.items.ItemBuilder;
import com.lib.utils.Formatter;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Skin {

    private String id;
    private String name;
    private double multiplier;
    private int customModelData;

    public Skin(String id, String name, double multiplier, int customModelData) {
        this.id = id;
        this.name = name;
        this.multiplier = multiplier;
        this.customModelData = customModelData;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public void setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
    }

    public ItemStack getItem(){
        ItemStack skin = new ItemBuilder(Material.DIAMOND)
                .setDisplayName(name)
                .setLore(
                        "",
                        "§fMultiplicador: §ex" + Formatter.formatDecimals(multiplier, 2, true),
                        "",
                        "§eClique em uma picareta para usar."
                )
                .setCustomModelData(customModelData)
                .build();

        NBTItem nbtItem = new NBTItem(skin);
        nbtItem.setString("omine_skin_item", this.id);
        nbtItem.setDouble("omine_skin_item_multiplier", this.multiplier);

        return nbtItem.getItem();
    }
}
