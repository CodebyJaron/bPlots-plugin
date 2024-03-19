package nl.bamischrijft.bplots.commands.subcommands;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nl.bamischrijft.bplots.Main;
import nl.bamischrijft.bplots.util.WorldGuardUtil;
import nl.bamischrijft.bplots.util.messages.Messages;
import nl.bamischrijft.bplots.util.subcommands.PlotSubCommand;
import nl.bamischrijft.bplots.util.messages.Message;
import nl.bamischrijft.bplots.util.messages.Placeholder;
import org.bukkit.entity.Player;

public class SetPriceCommand extends PlotSubCommand {
    public SetPriceCommand(String name, String description, String usage, String permission) {
        super(name, description, usage, permission);
    }

    @Override
    public boolean onCommand(Player player, ProtectedRegion region, String label, String[] args) throws Exception {
        if (args.length == 0) return false;

        Messages messages = Main.getMessages();

        long value;
        try {
            value = Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            player.sendMessage(messages.get(Message.NOT_AN_INTEGER, Placeholder.of("Input", args[0])));
            return true;
        }

        if (value < 0) {
            player.sendMessage(messages.get(Message.PLOT_SET_PRICE_NOT_POSITIVE));
            return true;
        }

        region.setFlag(WorldGuardUtil.PLOT_PRICE, value);
        player.sendMessage(messages.get(Message.PLOT_SET_PRICE_SUCCESS, Placeholder.of("Price", value)));

        return true;
    }
}
