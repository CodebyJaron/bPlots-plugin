package nl.bamischrijft.bplots.commands.subcommands;

import nl.bamischrijft.bplots.menus.PlotSalesMenu;
import nl.bamischrijft.bplots.util.subcommands.SubCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SaleMenuCommand extends SubCommand {
    public SaleMenuCommand(String name, String defaultDescription, String defaultUsage, String defaultPermission) {
        super(name, defaultDescription, defaultUsage, defaultPermission);
    }

    @Override
    public boolean onCommand(Player player, String label, String[] args) throws Exception {
        PlotSalesMenu plotSalesMenu = new PlotSalesMenu(0, player);
        Inventory inventory = plotSalesMenu.getInventory();

        if (inventory == null) return true;
        player.openInventory(inventory);

        return true;
    }
}
