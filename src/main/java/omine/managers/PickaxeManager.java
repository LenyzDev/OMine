package omine.managers;

import com.lib.items.ItemBuilder;
import com.lib.utils.Formatter;
import com.lib.utils.Percentage;
import com.lib.utils.ProgressBar;
import de.tr7zw.nbtapi.NBTItem;
import omine.events.BreakRegenBlockEvent;
import omine.events.PickaxeLevelUpEvent;
import omine.objects.Skin;
import omine.storages.BonusStorage;
import omine.storages.SkinStorage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Random;

public class PickaxeManager {

    private SkinStorage skinStorage;
    private Random random = new Random();

    public PickaxeManager(SkinStorage skinStorage){
        this.skinStorage = skinStorage;
    }

    public ItemStack createPickaxe(){
        ItemStack pickaxe = new ItemBuilder(Material.DIAMOND_PICKAXE)
                .setDisplayName("§e§lMINE PICKAXE")
                .setLore(
                        "",
                        "§fSkin: §7Nenhuma",
                        "§fMultiplicador: §ex0,00",
                        "",
                        "§fLevel: §e0",
                        "§fExperience: §e0,0",
                        "§f["+getProgressBar(1, 0.0)+"§f]",
                        "",
                        "§fFortune: §e0",
                        "§fEfficiency: §e0",
                        ""
                )
                .hideFlag(ItemFlag.HIDE_ENCHANTS)
                .hideFlag(ItemFlag.HIDE_ATTRIBUTES)
                .addEnchantment(Enchantment.DURABILITY, 10)
                .unbreakable()
                .build();

        NBTItem nbtItem = new NBTItem(pickaxe);
        nbtItem.setInteger("omine_level", 0);
        nbtItem.setDouble("omine_experience", 0.0);
        nbtItem.setString("omine_pickaxe_skin", "");
        nbtItem.setDouble("omine_skin_multiplier", 0.0);

        return nbtItem.getItem();
    }

    public boolean isPickaxe(Player player){
        ItemStack itemStack = player.getItemInHand();

        if(itemStack == null) return false;
        if(!itemStack.hasItemMeta()) return false;
        NBTItem nbt = new NBTItem(itemStack);

        return nbt.hasKey("omine_level") && nbt.hasKey("omine_experience");
    }

    public boolean isPickaxe(ItemStack itemStack){
        if(itemStack == null) return false;
        if(!itemStack.hasItemMeta()) return false;
        NBTItem nbt = new NBTItem(itemStack);

        return nbt.hasKey("omine_level") && nbt.hasKey("omine_experience");
    }

    public boolean isSkin(ItemStack itemStack){
        if(itemStack == null) return false;
        if(!itemStack.hasItemMeta()) return false;
        NBTItem nbt = new NBTItem(itemStack);

        return nbt.hasKey("omine_skin_item") && nbt.hasKey("omine_skin_item_multiplier");
    }

    public boolean setSkin(Player player, ItemStack itemStack, String skin, double multiplier){
        NBTItem nbt = new NBTItem(itemStack);

        if(!nbt.hasTag("omine_pickaxe_skin")){
            nbt.setString("omine_pickaxe_skin", "");
            nbt.setDouble("omine_skin_multiplier", 0.0);
        }

        String pickaxeSkin = nbt.getString("omine_pickaxe_skin");
        if(!pickaxeSkin.isEmpty()){
            if(skinStorage.isSkin(pickaxeSkin)){
                Skin oldSkin = skinStorage.getSkin(pickaxeSkin);
                player.getInventory().addItem(oldSkin.getItem());
            }
        }

        nbt.setString("omine_pickaxe_skin", skin);
        nbt.setDouble("omine_skin_multiplier", multiplier);

        ItemMeta itemMeta = setLore(nbt.getItem().getItemMeta(), nbt.getInteger("omine_level"), nbt.getDouble("omine_experience"), nbt, nbt.getItem());
        nbt.getItem().setItemMeta(itemMeta);

        itemStack.setItemMeta(nbt.getItem().getItemMeta());
        return true;
    }

    public void addExperience(Player player, double experience){
        ItemStack itemStack = player.getItemInHand();
        NBTItem nbt = new NBTItem(itemStack);

        int level = nbt.getInteger("omine_level");
        double currentExperience = nbt.getDouble("omine_experience");
        double newExperience = currentExperience + experience;
        if(!nbt.hasTag("omine_pickaxe_skin")){
            nbt.setString("omine_pickaxe_skin", "");
            nbt.setDouble("omine_skin_multiplier", 0.0);
        }

        if(newExperience >= getExperienceNeeded(level)){
            level += 1;
            newExperience -= getExperienceNeeded(level-1);

            nbt.setInteger("omine_level", level);
            nbt.setDouble("omine_experience", newExperience);

            if(random.nextInt(2) == 1){
                nbt.getItem().addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, itemStack.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)+1);
            }else{
                nbt.getItem().addUnsafeEnchantment(Enchantment.DIG_SPEED, itemStack.getEnchantmentLevel(Enchantment.DIG_SPEED)+1);
            }

            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            player.sendTitle("§e§lLEVEL UP", "§fᴘɪᴄᴋᴀxᴇ ʟᴇᴠᴇʟ: §7§m"+(level-1)+"§8 > §7"+level, 10, 40, 10);
            Bukkit.getPluginManager().callEvent(new PickaxeLevelUpEvent(player, level));
        }else{
            nbt.setDouble("omine_experience", newExperience);
        }

        ItemMeta itemMeta = setLore(nbt.getItem().getItemMeta(), level, newExperience, nbt, nbt.getItem());
        nbt.getItem().setItemMeta(itemMeta);
        player.setItemInHand(nbt.getItem());
    }

    public double getFortuneMultiplier(Player player){
        ItemStack itemStack = player.getItemInHand();
        return 1 + (itemStack.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) * 0.1);
    }

    public double getSkinMultiplier(Player player){
        ItemStack itemStack = player.getItemInHand();
        NBTItem nbt = new NBTItem(itemStack);
        return nbt.getDouble("omine_skin_multiplier");
    }

    private ItemMeta setLore(ItemMeta itemMeta, int level, double experience, NBTItem nbtItem, ItemStack pickaxe){
        String skin = nbtItem.getString("omine_pickaxe_skin");
        if(skin.isEmpty()){
            skin = "§7Nenhuma";
        }
        itemMeta.setLore(Arrays.asList(
                "",
                "§fSkin: §e"+skin,
                "§fMultiplicador: §ex"+Formatter.formatDecimals(nbtItem.getDouble("omine_skin_multiplier"), 2, true),
                "",
                "§fLevel: §e"+Formatter.formatDecimals(level, 0, true),
                "§fExperiencia: §e"+Formatter.formatDecimals(experience, 1, true),
                "§f["+getProgressBar(level, experience)+"§f]",
                "",
                "§fFortuna: §e"+Formatter.formatDecimals(pickaxe.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS), 0, true),
                "§fEficiencia: §e"+Formatter.formatDecimals(pickaxe.getEnchantmentLevel(Enchantment.DIG_SPEED), 0, true),
                ""
        ));
        return itemMeta;
    }

    private String getProgressBar(int level, double experience){
        return ProgressBar.getProgressBar(20, (short) Percentage.getPercentage(true, experience, getExperienceNeeded(level)), "|", "§7", "§a");
    }

    private double getExperienceNeeded(int level){
        return (level+1) * 1000;
    }
}
