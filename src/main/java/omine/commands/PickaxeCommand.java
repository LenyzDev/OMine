package omine.commands;

import com.lib.color.Colors;
import omine.managers.PickaxeManager;
import omine.storages.SkinStorage;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PickaxeCommand implements CommandExecutor {

    private SkinStorage skinStorage;
    private PickaxeManager pickaxeManager;

    public PickaxeCommand(SkinStorage skinStorage, PickaxeManager pickaxeManager) {
        this.skinStorage = skinStorage;
        this.pickaxeManager = pickaxeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0){
            if (!(sender instanceof Player)) {
                sender.sendMessage(Colors.process("&cYou must be a player to execute this command!"));
                return false;
            }
            Player player = (Player) sender;
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);
            player.sendActionBar(Colors.process("&e&lMINE &fYou have received a &e&lMINE PICKAXE&f!"));
            player.getInventory().addItem(pickaxeManager.createPickaxe());
            return true;
        }else{
            if(args[0].equalsIgnoreCase("giveskin")){
                if(!sender.hasPermission("omine.admin")) {
                    sender.sendMessage("§cYou do not have permission to execute this command.");
                    return false;
                }
                if(args.length != 3){
                    sender.sendMessage(Colors.process("&cUsage: /pickaxe giveskin <player> <skin>"));
                    return false;
                }

                Player target = Bukkit.getPlayer(args[1]);
                if(target == null){
                    sender.sendMessage(Colors.process("&cPlayer not found!"));
                    return false;
                }

                if(!skinStorage.isSkin(args[2])){
                    sender.sendMessage(Colors.process("&cSkin not found!"));
                    return false;
                }

                sender.sendMessage(Colors.process("&aSkin "+args[2]+" given to "+target.getName()));
                target.getInventory().addItem(skinStorage.getSkinItem(args[2]));
                return true;
            }
        }
        return false;
    }
}
