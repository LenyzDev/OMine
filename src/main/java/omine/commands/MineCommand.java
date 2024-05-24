package omine.commands;

import com.lib.color.Colors;
import omine.managers.MineManager;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MineCommand implements CommandExecutor {

    private MineManager mineManager;

    public MineCommand(MineManager mineManager) {
        this.mineManager = mineManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Colors.process("&cYou must be a player to execute this command!"));
            return false;
        }
        Player player = (Player) sender;
        player.sendTitle(Colors.process("&e&lMINE"), Colors.process("&fYou have been teleported to the mine!"), 10, 40, 10);
        player.teleport(mineManager.getMineLocation());
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
        return true;
    }
}