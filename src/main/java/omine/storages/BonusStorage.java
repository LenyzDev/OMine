package omine.storages;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class BonusStorage {

    private HashMap<String, Double> experienceBonus = new HashMap<>();
    private HashMap<String, Double> moneyBonus = new HashMap<>();
    private HashMap<String, Double> economyBonus = new HashMap<>();

    public BonusStorage(FileConfiguration config){
        for(String key : config.getConfigurationSection("experience-bonus").getKeys(false)) {
            experienceBonus.put(key, config.getDouble("experience-bonus."+key) / 100);
        }

        for(String key : config.getConfigurationSection("money-bonus").getKeys(false)) {
            moneyBonus.put(key, config.getDouble("money-bonus."+key) / 100);
        }

        for(String key : config.getConfigurationSection("economy-bonus").getKeys(false)) {
            economyBonus.put(key, config.getDouble("economy-bonus."+key) / 100);
        }
    }

    public double getExperienceBonus(String vip) {
        return experienceBonus.get(vip);
    }

    public double getMoneyBonus(String vip) {
        return moneyBonus.get(vip);
    }

    public double getEconomyBonus(String vip) {
        return economyBonus.get(vip);
    }

    public double getExperienceBonus(Player player) {
        double bonus = 0;

        for(String key : experienceBonus.keySet()) {
            if(player.hasPermission("omine.experience."+key) && experienceBonus.get(key) > bonus) {
                bonus = experienceBonus.get(key);
            }
        }

        return bonus;
    }

    public double getMoneyBonus(Player player) {
        double bonus = 0;

        for(String key : moneyBonus.keySet()) {
            if(player.hasPermission("omine.money."+key) && moneyBonus.get(key) > bonus) {
                bonus = moneyBonus.get(key);
            }
        }

        return bonus;
    }

    public double getEconomyBonus(Player player) {
        double bonus = 0;

        for(String key : economyBonus.keySet()) {
            if(player.hasPermission("omine.economy."+key) && economyBonus.get(key) > bonus) {
                bonus = economyBonus.get(key);
            }
        }

        return bonus;
    }


}
