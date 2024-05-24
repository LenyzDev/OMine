package omine.listeners;

import app.miyuki.miyukieconomy.common.api.MiyukiEconomyAPI;
import app.miyuki.miyukieconomy.common.currency.Currency;
import com.github.fierioziy.particlenativeapi.api.ParticleNativeAPI;
import com.lib.color.Colors;
import com.lib.utils.Formatter;
import de.tr7zw.nbtapi.NBTItem;
import omine.OMine;
import omine.events.BreakRegenBlockEvent;
import omine.managers.MineManager;
import omine.managers.PickaxeManager;
import omine.objects.BlockReward;
import omine.objects.BrokenBlock;
import omine.runnables.BlockRegenTask;
import omine.storages.BlockStorage;
import omine.storages.SkinStorage;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.Locale;

public class PlayerListeners implements Listener {

    private OMine oMine;
    private BlockRegenTask blockRegenTask;
    private PickaxeManager pickaxeManager;
    private MineManager mineManager;
    private BlockStorage blockStorage;
    private SkinStorage skinStorage;

    public PlayerListeners(OMine oMine, BlockRegenTask blockRegenTask, PickaxeManager pickaxeManager, MineManager mineManager, BlockStorage blockStorage, SkinStorage skinStorage) {
        this.oMine = oMine;
        this.blockRegenTask = blockRegenTask;
        this.pickaxeManager = pickaxeManager;
        this.mineManager = mineManager;
        this.blockStorage = blockStorage;
        this.skinStorage = skinStorage;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBucketEvent(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();
        if(player.getGameMode().equals(GameMode.CREATIVE)) return;
        if(player.getWorld().getName().equalsIgnoreCase(mineManager.getMineWorld())){
            event.setCancelled(true);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, 1.0F);
            player.sendActionBar(Colors.process("&c&lMINE &fYou cannot place blocks here."));
            oMine.getParticleNativeAPI().LIST_1_8.REDSTONE.packet(true, getCenter(event.getBlock().getLocation()), 0.5D, 0.5D, 0.5D, 15).sendTo(player);
            return;
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(player.getGameMode().equals(GameMode.CREATIVE)) return;
        if(player.getWorld().getName().equalsIgnoreCase(mineManager.getMineWorld())){
            event.setCancelled(true);
            if(blockStorage.isBlock(event.getBlock().getType().toString())){
                if(!pickaxeManager.isPickaxe(player)){
                    player.sendTitle(Colors.process("&c&lMINE"), Colors.process("&fYou need a &e&lMINE PICKAXE&f to break this block."), 0, 40, 10);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, 1.0F);
                    oMine.getParticleNativeAPI().LIST_1_8.REDSTONE.packet(true, getCenter(event.getBlock().getLocation()), 0.5D, 0.5D, 0.5D, 15).sendTo(player);
                    return;
                }
                BlockReward blockReward = blockStorage.getBlock(event.getBlock().getType().toString());
                if(!player.hasPermission(blockReward.getPermission())){
                    player.sendTitle(Colors.process("&c&lMINE"), Colors.process(blockReward.getErrorMessage()), 0, 40, 10);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, 1.0F);
                    oMine.getParticleNativeAPI().LIST_1_8.REDSTONE.packet(true, getCenter(event.getBlock().getLocation()), 0.5D, 0.5D, 0.5D, 15).sendTo(player);
                    return;
                }

                Bukkit.getPluginManager().callEvent(new BreakRegenBlockEvent(player, blockReward, event.getBlock().getLocation()));
                blockRegenTask.addBrokenBlock(event.getBlock().getLocation(), new BrokenBlock(event.getBlock().getLocation(), event.getBlock().getType(), blockReward.getDelay() * 1000L, System.currentTimeMillis()));
                event.getBlock().setType(Material.BEDROCK);

                double money = blockReward.getMoney();
                double economy = blockReward.getEconomy();
                double experience = blockReward.getExperience();

                double fortuneMultiplier = pickaxeManager.getFortuneMultiplier(player);
                money *= fortuneMultiplier;

                double skinMultiplier = pickaxeManager.getSkinMultiplier(player);
                if(skinMultiplier > 0) economy *= skinMultiplier;

                String actionbar = "";

                if(blockReward.getMoney() > 0){
                    if(oMine.getBonusStorage().getMoneyBonus(player) > 0){
                        money += money * oMine.getBonusStorage().getMoneyBonus(player);
                    }
                    oMine.getEconomy().depositPlayer(player, money);
                    actionbar += "§a+" + Formatter.formatDecimals(money, 0, true) + " ᴍᴏɴᴇʏ";
                }

                if(mineManager.isUseEconomy() && blockReward.getEconomy() > 0){
                    if(oMine.getBonusStorage().getEconomyBonus(player) > 0){
                        economy += economy * oMine.getBonusStorage().getEconomyBonus(player);
                    }
                    oMine.getMiyukiEconomyAPI().addBalance(oMine.getMiyukiEconomyAPI().getUser(player.getName()), oMine.getCurrency(), BigDecimal.valueOf(economy));
                    actionbar += "§7 | §d+" + Formatter.formatDecimals(economy, 0, true) + " ᴄʀʏsᴛᴀʟs";
                }

                if(blockReward.getExperience() > 0){
                    if(oMine.getBonusStorage().getExperienceBonus(player) > 0){
                        experience += experience * oMine.getBonusStorage().getExperienceBonus(player);
                    }
                    pickaxeManager.addExperience(player, experience);
                    actionbar += "§7 | §b+" + Formatter.formatDecimals(experience, 1, true) + " ᴇxᴘ";
                }

                if(blockReward.getItem1()){
                    double random = getRandomDouble(0, 10000);
                    if(random <= (500 + fortuneMultiplier)){
                        player.getInventory().addItem(mineManager.getItem1());
                    }
                }

                if(blockReward.getItem2()){
                    double random = getRandomDouble(0, 10000);
                    if(random <= (250 + fortuneMultiplier)){
                        player.getInventory().addItem(mineManager.getItem2());
                    }
                }

                if(blockReward.getItem3()){
                    double random = getRandomDouble(0, 10000);
                    if(random <= (100 + fortuneMultiplier)){
                        player.getInventory().addItem(mineManager.getItem3());
                    }
                }

                player.sendActionBar(Colors.process(actionbar));

            }else{
                player.sendActionBar(Colors.process("&c&lMINE &fYou cannot break blocks here."));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, 1.0F);
                oMine.getParticleNativeAPI().LIST_1_8.REDSTONE.packet(true, getCenter(event.getBlock().getLocation()), 0.5D, 0.5D, 0.5D, 15).sendTo(player);
                return;
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if(player.getGameMode().equals(GameMode.CREATIVE)) return;
        if(player.getWorld().getName().equalsIgnoreCase(mineManager.getMineWorld())){
            event.setCancelled(true);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, 1.0F);
            player.sendTitle(Colors.process("&c&lMINE"), Colors.process("&fYou cannot place blocks here."), 0, 40, 10);
            oMine.getParticleNativeAPI().LIST_1_8.REDSTONE.packet(true, getCenter(event.getBlock().getLocation()), 0.5D, 0.5D, 0.5D, 15).sendTo(player);
            return;
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if(event.getAction() != InventoryAction.SWAP_WITH_CURSOR) return;
        ItemStack cursorItem = event.getCursor();
        ItemStack inventoryItem = event.getCurrentItem();
        if(!pickaxeManager.isSkin(cursorItem)) return;
        if(!pickaxeManager.isPickaxe(inventoryItem)) return;

        NBTItem cursorNBT = new NBTItem(cursorItem);
        String skinName = cursorNBT.getString("omine_skin_item");
        if(!skinStorage.isSkin(skinName)) return;
        double skinMultiplier = skinStorage.getSkin(skinName).getMultiplier();

        event.setCancelled(true);
        if(pickaxeManager.setSkin(player, inventoryItem, skinName, skinMultiplier)){
            cursorItem.setAmount(cursorItem.getAmount()-1);
        }
    }

    @EventHandler
    public void onEnchantEvent(PrepareItemEnchantEvent event) {
        ItemStack itemStack = event.getInventory().getItem(0);
        if (itemStack == null) return;
        if (!pickaxeManager.isPickaxe(itemStack)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onAnvilEvent(PrepareAnvilEvent event) {
        ItemStack itemStack = event.getInventory().getItem(0);
        if (itemStack == null) return;
        if (!pickaxeManager.isPickaxe(itemStack)) return;
        event.setResult(null);
    }

    @EventHandler
    public void onGrindstoneEvent(PrepareGrindstoneEvent event) {
        ItemStack itemStack = event.getInventory().getItem(0);
        if (itemStack == null) return;
        if (!pickaxeManager.isPickaxe(itemStack)) return;
        event.setResult(null);
    }

    public Location getCenter(Location location) {
        return new Location(location.getWorld(), location.getBlockX() + 0.5D, location.getBlockY() + 0.5D, location.getBlockZ() + 0.5D);
    }

    private double getRandomDouble(double min, double max) {
        return Math.random() * (max - min) + min;
    }

}
