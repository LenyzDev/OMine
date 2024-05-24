package omine;

import app.miyuki.miyukieconomy.bukkit.BukkitMiyukiEconomy;
import app.miyuki.miyukieconomy.bukkit.BukkitMiyukiEconomyBootstrap;
import app.miyuki.miyukieconomy.common.api.MiyukiEconomyAPI;
import app.miyuki.miyukieconomy.common.currency.Currency;
import app.miyuki.miyukieconomy.common.currency.CurrencyManager;
import com.github.fierioziy.particlenativeapi.api.ParticleNativeAPI;
import com.github.fierioziy.particlenativeapi.plugin.ParticleNativePlugin;
import net.milkbowl.vault.economy.Economy;
import omine.commands.CreationCommand;
import omine.commands.MineCommand;
import omine.commands.PickaxeCommand;
import omine.configuration.ConfigManager;
import omine.database.BlockDAO;
import omine.database.DatabaseManager;
import omine.listeners.PlayerListeners;
import omine.managers.MineManager;
import omine.managers.PickaxeManager;
import omine.runnables.BlockRegenTask;
import omine.storages.BlockStorage;
import omine.storages.BonusStorage;
import omine.storages.CreationStorage;
import omine.storages.SkinStorage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Logger;

public final class OMine extends JavaPlugin {

    private Logger logger;
    private ConfigManager configManager;
    private MineManager mineManager;
    private PickaxeManager pickaxeManager;
    private BlockStorage blockStorage;
    private BonusStorage bonusStorage;
    private SkinStorage skinStorage;
    private CreationStorage creationStorage;
    private BlockDAO blockDAO;
    private BlockRegenTask blockRegenTask;

    private Economy econ = null;
    private ParticleNativeAPI particleNativeAPI;
    private BukkitMiyukiEconomy bukkitMiyukiEconomy;
    private CurrencyManager currencyManager;
    private Currency currency;

    @Override
    public void onEnable() {
        this.logger = getLogger();
        this.configManager = new ConfigManager(this, logger);

        if (!setupEconomy() ) {
            logger.info("Vault not found.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if(configManager.getConfig("config").getBoolean("use-economy")){
            bukkitMiyukiEconomy = ((BukkitMiyukiEconomyBootstrap)JavaPlugin.getPlugin(BukkitMiyukiEconomyBootstrap.class)).getMiyukiEconomy();
            currencyManager = bukkitMiyukiEconomy.getCurrencyManager();
            currency = currencyManager.getCurrencies().get(configManager.getConfig("config").getString("economy"));
            if(currency == null){
                logger.warning("Currency not found!");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }

        Plugin particlePlugin = this.getServer().getPluginManager().getPlugin("ParticleNativeAPI");
        if (particlePlugin != null) {
            if (!ParticleNativePlugin.isValid()) logger.warning("ParticleNativeAPI is not enabled!");
            particleNativeAPI = ParticleNativePlugin.getAPI();
        }else{
            logger.warning("ParticleNativeAPI is not installed!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.blockRegenTask = new BlockRegenTask(particleNativeAPI);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, blockRegenTask, 20, 20);

        blockStorage = new BlockStorage(configManager.getConfig("config"));
        bonusStorage = new BonusStorage(configManager.getConfig("config"));
        skinStorage = new SkinStorage(configManager.getConfig("config"));

        mineManager = new MineManager(configManager.getConfig("config"));
        pickaxeManager = new PickaxeManager(skinStorage);

        creationStorage = new CreationStorage(mineManager, configManager.getConfig("config"));

        DatabaseManager.connect(logger, configManager.getConfig("config"));
        this.blockDAO = new BlockDAO(logger);
        this.blockDAO.loadBrokenBlocks(blockRegenTask);

        this.getServer().getPluginManager().registerEvents(new PlayerListeners(this, blockRegenTask, pickaxeManager, mineManager, blockStorage, skinStorage), this);
        this.getCommand("pickaxe").setExecutor(new PickaxeCommand(skinStorage, pickaxeManager));
        this.getCommand("mine").setExecutor(new MineCommand(mineManager));
        this.getCommand("minecreate").setExecutor(new CreationCommand(creationStorage));

        autoReconnect();
    }

    @Override
    public void onDisable() {
        this.blockDAO.unloadBrokenBlocks(blockRegenTask);
        DatabaseManager.disconnect(logger);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEconomy() {
        return econ;
    }

    public MiyukiEconomyAPI getMiyukiEconomyAPI() {
        return bukkitMiyukiEconomy.getMiyukiEconomyAPI();
    }

    public Currency getCurrency() {
        return currency;
    }

    public ParticleNativeAPI getParticleNativeAPI() {
        return particleNativeAPI;
    }

    private void autoReconnect() {
        new BukkitRunnable() {
            @Override
            public void run() {
                DatabaseManager.reloadConnection(logger, configManager.getConfig("config"));
            }
        }.runTaskTimerAsynchronously(this, 20 * 60 * 60, 20 * 60 * 60);
    }

    public BonusStorage getBonusStorage() {
        return bonusStorage;
    }
}
