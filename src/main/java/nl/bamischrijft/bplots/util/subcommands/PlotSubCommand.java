package nl.bamischrijft.bplots.util.subcommands;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nl.bamischrijft.bplots.util.WorldGuardUtil;
import nl.bamischrijft.bplots.Main;
import nl.bamischrijft.bplots.util.messages.Message;
import org.bukkit.entity.Player;

public abstract class PlotSubCommand extends SubCommand {
    public PlotSubCommand(String name, String defaultDescription, String defaultUsage, String defaultPermission) {
        super(name, defaultDescription, defaultUsage, defaultPermission);
    }

    @Override
    public boolean onCommand(Player player, String label, String[] args) throws Exception {
        ProtectedRegion region = WorldGuardUtil.getRegion(player.getLocation());
        if (region == null) {
            player.sendMessage(Main.getMessages().get(Message.NOT_ON_A_PLOT));
            return true;
        }

        return onCommand(player, region, label, args);
    }

    public abstract boolean onCommand(Player player, ProtectedRegion region, String label, String[] args) throws Exception;
}

