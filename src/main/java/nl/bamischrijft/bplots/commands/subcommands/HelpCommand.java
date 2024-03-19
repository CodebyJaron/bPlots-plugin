package nl.bamischrijft.bplots.commands.subcommands;

import nl.bamischrijft.bplots.Main;
import nl.bamischrijft.bplots.commands.PlotCommand;
import nl.bamischrijft.bplots.util.messages.Message;
import nl.bamischrijft.bplots.util.messages.Messages;
import nl.bamischrijft.bplots.util.messages.Placeholder;
import nl.bamischrijft.bplots.util.subcommands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.StringJoiner;

public class HelpCommand extends SubCommand {
    private final PlotCommand command;

    public HelpCommand(PlotCommand command, String name, String defaultDescription, String defaultUsage, String defaultPermission) {
        super(name, defaultDescription, defaultUsage, defaultPermission);

        this.command = command;
    }

    @Override
    public boolean onCommand(Player player, String label, String[] args) throws Exception {
        Messages messages = Main.getMessages();

        StringJoiner joiner = new StringJoiner("\n", ChatColor.AQUA + "bPlots Help\n", "");
        for (SubCommand subCommand : command.subCommands) {
            joiner.add(messages.get(Message.HELP_COMMAND_COMMAND_FORMAT, Placeholder.of("Command", label),
                    Placeholder.of("SubCommand", subCommand.getName() + (subCommand.getUsage().length() == 0 ? "" : " " + subCommand.getUsage())),
                    Placeholder.of("Description", subCommand.getDescription()), Placeholder.of("Permission", subCommand.getPermission())));
        }

        player.sendMessage(joiner.toString());


        return true;
    }
}
