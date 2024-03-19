package nl.bamischrijft.bplots.commands.subcommands;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nl.bamischrijft.bplots.Main;
import nl.bamischrijft.bplots.util.WorldGuardUtil;
import nl.bamischrijft.bplots.util.database.objects.Sale;
import nl.bamischrijft.bplots.util.messages.Message;
import nl.bamischrijft.bplots.util.messages.Messages;
import nl.bamischrijft.bplots.util.messages.Placeholder;
import nl.bamischrijft.bplots.util.subcommands.PlotSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BuyCommand extends PlotSubCommand {
    public BuyCommand(String name, String defaultDescription, String defaultUsage, String defaultPermission) {
        super(name, defaultDescription, defaultUsage, defaultPermission);
    }

    @Override
    public boolean onCommand(Player player, ProtectedRegion region, String label, String[] args) throws Exception {
        Messages messages = Main.getMessages();

        if (region.isOwner(WorldGuardPlugin.inst().wrapPlayer(player))) {
            player.sendMessage(messages.get(Message.PLOT_CANT_BUY_OWN_PLOT));
            return true;
        }

        if (WorldGuardUtil.getRegionAmount(player) >= Main.getPlugin().getConfig().getInt("settings.buy.max-plots")) {
            player.sendMessage(Main.getMessages().get(Message.PLOT_BUY_TO_MUCH_PLOTS));
            return true;
        }

        Long price = region.getFlag(WorldGuardUtil.PLOT_PRICE);
        Sale sale = Main.getSaleManager().getSale(region);
        if ((region.getOwners().size() > 0 || price == null) && sale == null) {
            player.sendMessage(messages.get(Message.PLOT_NOT_FOR_SALE));
            return true;
        }

        if (price == null) price = sale.getPrice();

        if (args.length == 1 && args[0].equalsIgnoreCase("confirm")) {
            if (Main.getEconomy().getBalance(player) < price) {
                player.sendMessage(messages.get(Message.NOT_ENOUGH_MONEY));
                return true;
            }

            if (WorldGuardUtil.getRegionAmount(player) > Main.getPlugin().getConfig().getInt("settings.buy.max-plots")) {
                player.sendMessage(Main.getMessages().get(Message.PLOT_BUY_TO_MUCH_PLOTS));
                return true;
            }

            Main.getEconomy().withdrawPlayer(player, price);
            if (sale != null) Main.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(sale.getSeller()), price);

            region.getOwners().clear();
            region.getMembers().clear();
            region.getOwners().addPlayer(player.getUniqueId());
            if (sale != null) Main.getSaleManager().removeSale(region);

            player.sendMessage(messages.get(Message.PLOT_BUY_SUCCESS,
                    Placeholder.of("PlotName", region.getId()), Placeholder.of("Price", price)));
        } else {
            player.sendMessage(messages.get(Message.PLOT_BUY_CONFIRM,
                    Placeholder.of("Command", label),
                    Placeholder.of("PlotName", region.getId()), Placeholder.of("Price", price)));
        }

        return true;
    }
}
