package omine.commands;

import com.lib.color.Colors;
import omine.storages.CreationStorage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreationCommand implements CommandExecutor {

    private CreationStorage creationStorage;

    public CreationCommand(CreationStorage creationStorage) {
        this.creationStorage = creationStorage;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Colors.process("&cYou must be a player to execute this command!"));
            return false;
        }
        Player player = (Player) sender;
        if(args.length != 0){
            String creationName = args[0];
            if(!creationStorage.hasCreation(creationName)) {
                player.sendMessage(Colors.process("&cCreation not found!"));
                return false;
            }
            if(!player.hasPermission(creationStorage.getCreation(creationName).getPermission())) {
                player.sendMessage(Colors.process("&b&lESTAÇÃO &cVocê não tem Prestigio suficiente!"));
                return false;
            }

            boolean created = creationStorage.makeCreation(creationName, player);
            if(created) {
                player.sendMessage(Colors.process("&b&lESTAÇÃO &fVocê criou esse Item!"));
                return true;
            }else{
                player.sendMessage(Colors.process("&b&lESTAÇÃO &fVocê não pode criar esse Item!"));
                return false;
            }
        }
        return false;
    }
}
