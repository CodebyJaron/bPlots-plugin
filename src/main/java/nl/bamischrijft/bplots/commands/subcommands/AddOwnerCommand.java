package nl.bamischrijft.bplots.commands.subcommands;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nl.bamischrijft.bplots.Main;
import nl.bamischrijft.bplots.util.messages.Message;
import nl.bamischrijft.bplots.util.messages.Messages;
import nl.bamischrijft.bplots.util.messages.Placeholder;
import nl.bamischrijft.bplots.util.subcommands.PlotSubCommand;
import nl.bamischrijft.bplots.util.Util;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class AddOwnerCommand extends PlotSubCommand {
    public AddOwnerCommand(String name, String defaultDescription, String defaultUsage, String defaultPermission) {
        super(name, defaultDescription, defaultUsage, defaultPermission);
    }

    @Override
    public boolean onCommand(Player player, ProtectedRegion region, String label, String[] args) throws Exception {
        Messages messages = Main.getMessages();

        if (args.length == 0) return false;

        OfflinePlayer target = Util.getOfflinePlayer(args[0]);
        if (target == null || !target.hasPlayedBefore() && !target.isOnline()) {
            player.sendMessage(messages.get(Message.UNKNOWN_PLAYER, Placeholder.of("Player", args[0])));
            return true;
        }

        region.getOwners().addPlayer(target.getUniqueId());

        player.sendMessage(messages.get(Message.PLOT_ADD_OWNER,
                Placeholder.of("PlotName", region.getId()), Placeholder.of("NewOwner", target.getName())));

        return true;
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        if (args.length == 0) return Util.getOnlinePlayerNames();

        return Collections.emptyList();
    }
}
