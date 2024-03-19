package nl.bamischrijft.bplots.commands.subcommands;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nl.bamischrijft.bplots.Main;
import nl.bamischrijft.bplots.managers.SaleManager;
import nl.bamischrijft.bplots.util.messages.Message;
import nl.bamischrijft.bplots.util.messages.Messages;
import nl.bamischrijft.bplots.util.messages.Placeholder;
import nl.bamischrijft.bplots.util.subcommands.PlotSubCommand;
import org.bukkit.entity.Player;

public class StopSaleCommand extends PlotSubCommand {
    public StopSaleCommand(String name, String defaultDescription, String defaultUsage, String defaultPermission) {
        super(name, defaultDescription, defaultUsage, defaultPermission);
    }

    @Override
    public boolean onCommand(Player player, ProtectedRegion region, String label, String[] args) throws Exception {
        Messages messages = Main.getMessages();
        SaleManager saleManager = Main.getSaleManager();

        if (!region.isOwner(WorldGuardPlugin.inst().wrapPlayer(player))) {
            player.sendMessage(messages.get(Message.NOT_AN_OWNER));
            return true;
        }

        if (!saleManager.isForSale(region)) {
            player.sendMessage(messages.get(Message.PLOT_NOT_YET_FOR_SALE,
                    Placeholder.of("Command", label), Placeholder.of("PlotName", region.getId())));
            return true;
        }

        saleManager.removeSale(region);
        player.sendMessage(messages.get(Message.PLOT_REMOVED_FROM_SALE, Placeholder.of("PlotName", region.getId())));

        return true;
    }
}
