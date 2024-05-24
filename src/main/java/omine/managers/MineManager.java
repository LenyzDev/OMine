package omine.managers;

import com.lib.color.Colors;
import com.lib.items.ItemBuilder;
import com.lib.utils.LocationSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MineManager {

    private String mineWorld;
    private Location mineLocation;
    private boolean useEconomy;
    private ItemStack item1;
    private ItemStack item2;
    private ItemStack item3;

    public MineManager(FileConfiguration config) {
        this.mineWorld = config.getString("mine-world");
        this.mineLocation = LocationSerializer.deserialize(config.getString("mine-location"));
        this.mineLocation = LocationSerializer.validate(this.mineLocation);

        this.item1 = new ItemBuilder(Material.FIREWORK_STAR)
                .setDisplayName(Colors.process("&7&lMINÉRIO SIMPLES"))
                .setLore(Colors.process(List.of(
                        "",
                        "&fUse esse minério para",
                        "&fcriar itens na Mina.",
                        ""
                )))
                .setCustomModelData(9827340)
                .hideAllFlags()
                .build();

        this.item2 = new ItemBuilder(Material.GLOWSTONE_DUST    )
                .setDisplayName(Colors.process("&e&lMINÉRIO RARO"))
                .setLore(Colors.process(List.of(
                        "",
                        "&fUse esse minério para",
                        "&fcriar itens na Mina.",
                        ""
                )))
                .setCustomModelData(9827340)
                .hideAllFlags()
                .build();

        this.item3 = new ItemBuilder(Material.FIREWORK_STAR)
                .setDisplayName(Colors.process("&6&lMINÉRIO COMPLEXO"))
                .setLore(Colors.process(List.of(
                        "",
                        "&fUse esse minério para",
                        "&fcriar itens na Mina.",
                        ""
                )))
                .setCustomModelData(9827340)
                .hideAllFlags()
                .build();

        this.useEconomy = config.getBoolean("use-economy");
    }

    public String getMineWorld() {
        return mineWorld;
    }

    public Location getMineLocation() {
        return mineLocation;
    }

    public boolean isUseEconomy() {
        return useEconomy;
    }

    public ItemStack getItem1() {
        return item1;
    }

    public ItemStack getItem2() {
        return item2;
    }

    public ItemStack getItem3() {
        return item3;
    }
}
