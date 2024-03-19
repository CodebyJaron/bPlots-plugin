package nl.bamischrijft.bplots.commands.subcommands;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nl.bamischrijft.bplots.Main;
import nl.bamischrijft.bplots.util.database.objects.Sale;
import nl.bamischrijft.bplots.util.messages.Message;
import nl.bamischrijft.bplots.util.messages.Messages;
import nl.bamischrijft.bplots.util.messages.Placeholder;
import nl.bamischrijft.bplots.util.subcommands.PlotSubCommand;
import nl.bamischrijft.bplots.managers.SaleManager;
import org.bukkit.entity.Player;

public class SellCommand extends PlotSubCommand {
    private final int MAX_AMOUNT_OF_SALES;

    public SellCommand(String name, String defaultDescription, String defaultUsage, String defaultPermission) {
        super(name, defaultDescription, defaultUsage, defaultPermission);

        MAX_AMOUNT_OF_SALES = Main.getPlugin().getConfig().getInt("settings.sell.max-sales-per-player", -1);
    }

    @Override
    public boolean onCommand(Player player, ProtectedRegion region, String label, String[] args) throws Exception {
        if (args.length == 0) return false;

        Messages messages = Main.getMessages();
        SaleManager saleManager = Main.getSaleManager();

        if (!region.isOwner(WorldGuardPlugin.inst().wrapPlayer(player))) {
            player.sendMessage(messages.get(Message.NOT_AN_OWNER));
            return true;
        }

        if (saleManager.isForSale(region)) {
            player.sendMessage(messages.get(Message.PLOT_ALREADY_FOR_SALE,
                    Placeholder.of("Command", label), Placeholder.of("PlotName", region.getId())));
            return true;
        }

        if (MAX_AMOUNT_OF_SALES > 0 && saleManager.getSales()
                .stream().filter(sale -> sale.getSeller() == player.getUniqueId()).count() > MAX_AMOUNT_OF_SALES) {
            player.sendMessage(messages.get(Message.PLOT_MAX_SALES, Placeholder.of("SaleCount", MAX_AMOUNT_OF_SALES)));
            return true;
        }

        long value;
        try {
            value = Long.parseLong(args[0]);
        } catch (NumberFormatException ex) {
            player.sendMessage(messages.get(Message.NOT_AN_INTEGER, Placeholder.of("Input", args[0])));
            return true;
        }

        if (value < 0) {
            player.sendMessage(messages.get(Message.PLOT_SELL_PRICE_NOT_POSITIVE));
            return true;
        }

        saleManager.addSale(new Sale(player.getWorld(), region, player.getUniqueId(), value));
        player.sendMessage(messages.get(Message.PLOT_SELL_SUCCESS,
                Placeholder.of("PlotName", region.getId()), Placeholder.of("Price", value)));

        return true;
    }
}
