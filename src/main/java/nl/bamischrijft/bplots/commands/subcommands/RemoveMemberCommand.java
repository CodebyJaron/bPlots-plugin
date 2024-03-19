package nl.bamischrijft.bplots.commands.subcommands;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nl.bamischrijft.bplots.Main;
import nl.bamischrijft.bplots.util.Util;
import nl.bamischrijft.bplots.util.messages.Message;
import nl.bamischrijft.bplots.util.messages.Messages;
import nl.bamischrijft.bplots.util.messages.Placeholder;
import nl.bamischrijft.bplots.util.subcommands.PlotSubCommand;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class RemoveMemberCommand extends PlotSubCommand {
    private String permission;

    public RemoveMemberCommand(String name, String defaultDescription, String defaultUsage, String defaultPermission) {
        super(name, defaultDescription, defaultUsage, defaultPermission);

        FileConfiguration config = Main.getPlugin().getConfig();

        String permissionPath = "commands.plot.subcommands." + name + ".no-owner-permission";
        permission = config.getString(permissionPath);
        if (permission == null) {
            config.set(permissionPath, defaultPermission);
            permission = defaultPermission;
        }
    }

    @Override
    public boolean onCommand(Player player, ProtectedRegion region, String label, String[] args) throws Exception {
        Messages messages = Main.getMessages();

        if (!region.isOwner(WorldGuardPlugin.inst().wrapPlayer(player)) && !player.hasPermission(permission)) {
            player.sendMessage(messages.get(Message.NOT_AN_OWNER));
            return true;
        }

        if (args.length == 0) return false;

        OfflinePlayer target = Util.getOfflinePlayer(args[0]);
        if (target == null || !target.hasPlayedBefore() && !target.isOnline()) {
            player.sendMessage(messages.get(Message.UNKNOWN_PLAYER, Placeholder.of("Player", args[0])));
            return true;
        }

        region.getMembers().removePlayer(target.getUniqueId());

        player.sendMessage(messages.get(Message.PLOT_REMOVE_MEMBER,
                Placeholder.of("PlotName", region.getId()), Placeholder.of("OldMember", target.getName())));

        return true;
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        if (args.length == 0) return Util.getOnlinePlayerNames();

        return Collections.emptyList();
    }
}
