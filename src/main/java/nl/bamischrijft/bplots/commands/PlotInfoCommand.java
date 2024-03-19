package nl.bamischrijft.bplots.commands;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nl.bamischrijft.bplots.Main;
import nl.bamischrijft.bplots.util.WorldGuardUtil;
import nl.bamischrijft.bplots.util.messages.Message;
import nl.bamischrijft.bplots.menus.PlotInfoMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlotInfoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Main.getMessages().get(Message.NO_CONSOLE));
            return true;
        }

        Player player = (Player) sender;

        ProtectedRegion region;
        try {
            region = WorldGuardUtil.getRegion(player.getLocation());
            if (region == null) {
                player.sendMessage(Main.getMessages().get(Message.NOT_ON_A_PLOT));
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            player.sendMessage(Main.getMessages().get(Message.UNKNOWN_ERROR));
            return true;
        }

        ((Player) sender).openInventory(new PlotInfoMenu(player, region).getInventory());
        return true;
    }
}
