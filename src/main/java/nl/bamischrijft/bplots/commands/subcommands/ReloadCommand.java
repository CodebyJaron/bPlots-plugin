package nl.bamischrijft.bplots.commands.subcommands;

import nl.bamischrijft.bplots.Main;
import nl.bamischrijft.bplots.util.messages.Message;
import nl.bamischrijft.bplots.util.subcommands.SubCommand;
import org.bukkit.entity.Player;

public class ReloadCommand extends SubCommand {
    public ReloadCommand(String name, String defaultDescription, String defaultUsage, String defaultPermission) {
        super(name, defaultDescription, defaultUsage, defaultPermission);
    }

    @Override
    public boolean onCommand(Player player, String label, String[] args) throws Exception {
        Main.reload();
        player.sendMessage(Main.getMessages().get(Message.RELOAD_SUCCESS));

        return true;
    }
}
