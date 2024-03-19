package nl.bamischrijft.bplots.commands.subcommands;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nl.bamischrijft.bplots.util.subcommands.PlotSubCommand;
import nl.bamischrijft.bplots.menus.PlotInfoMenu;
import org.bukkit.entity.Player;

public class PlotInfoCommand extends PlotSubCommand {
    public PlotInfoCommand(String name, String defaultDescription, String defaultUsage, String defaultPermission) {
        super(name, defaultDescription, defaultUsage, defaultPermission);
    }

    @Override
    public boolean onCommand(Player player, ProtectedRegion region, String label, String[] args) throws Exception {
        player.openInventory(new PlotInfoMenu(player, region).getInventory());

        return true;
    }
}
