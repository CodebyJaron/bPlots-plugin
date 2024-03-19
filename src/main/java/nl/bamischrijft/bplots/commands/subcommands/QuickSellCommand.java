package nl.bamischrijft.bplots.commands.subcommands;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nl.bamischrijft.bplots.Main;
import nl.bamischrijft.bplots.util.WorldGuardUtil;
import nl.bamischrijft.bplots.util.messages.Message;
import nl.bamischrijft.bplots.util.messages.Messages;
import nl.bamischrijft.bplots.util.messages.Placeholder;
import nl.bamischrijft.bplots.util.subcommands.PlotSubCommand;
import org.bukkit.entity.Player;

public class QuickSellCommand extends PlotSubCommand {
    private final int QUICKSELL_PERCENTAGE;

    public QuickSellCommand(String name, String defaultDescription, String defaultUsage, String defaultPermission) {
        super(name, defaultDescription, defaultUsage, defaultPermission);

        QUICKSELL_PERCENTAGE = Main.getPlugin().getConfig().getInt("settings.quicksell.percentage", 80);
    }

    @Override
    public boolean onCommand(Player player, ProtectedRegion region, String label, String[] args) throws Exception {
        Messages messages = Main.getMessages();

        if (!region.isOwner(WorldGuardPlugin.inst().wrapPlayer(player))) {
            player.sendMessage(messages.get(Message.NOT_AN_OWNER));
            return true;
        }

        Long price = region.getFlag(WorldGuardUtil.PLOT_PRICE);
        if (price == null) {
            player.sendMessage(messages.get(Message.PLOT_NO_PRICE_SET));
            return true;
        }

        if (Main.getSaleManager().isForSale(region)) {
            player.sendMessage(messages.get(Message.PLOT_CANNOT_QUICKSELL_IF_FOR_SALE));
            return true;
        }

        int quickSellPrice = (int) Math.floor(price * (QUICKSELL_PERCENTAGE / 100.0));
        if (args.length == 1 && args[0].equalsIgnoreCase("confirm")) {
            Main.getEconomy().depositPlayer(player, quickSellPrice);
            region.getOwners().clear();
            region.getMembers().clear();

            player.sendMessage(messages.get(Message.PLOT_QUICKSELL_SUCCESS,
                    Placeholder.of("PlotName", region.getId()),
                    Placeholder.of("QuickSellPrice", quickSellPrice),
                    Placeholder.of("Price", price), Placeholder.of("QuickSellPercentage", QUICKSELL_PERCENTAGE)));
        } else {
            player.sendMessage(messages.get(Message.PLOT_QUICKSELL_CONFIRM,
                    Placeholder.of("Command", label), Placeholder.of("PlotName", region.getId()),
                    Placeholder.of("QuickSellPrice", quickSellPrice),
                    Placeholder.of("Price", price), Placeholder.of("QuickSellPercentage", QUICKSELL_PERCENTAGE)));
        }

        return true;
    }
}
