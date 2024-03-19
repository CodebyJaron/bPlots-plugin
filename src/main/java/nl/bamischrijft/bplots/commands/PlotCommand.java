package nl.bamischrijft.bplots.commands;

import nl.bamischrijft.bplots.commands.subcommands.*;
import nl.bamischrijft.bplots.Main;
import nl.bamischrijft.bplots.commands.subcommands.PlotInfoCommand;
import nl.bamischrijft.bplots.util.messages.Message;
import nl.bamischrijft.bplots.util.messages.Messages;
import nl.bamischrijft.bplots.util.messages.Placeholder;
import nl.bamischrijft.bplots.util.subcommands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/**
 *
 * bPlots
 * © 2021 BamiSchijf.me (</BamiSchijf>#2826)
 *
 * Raak dit niet aan!
 *
 * © Copyrighted
 */
public class PlotCommand implements CommandExecutor, TabCompleter {
    public final List<SubCommand> subCommands = Arrays.asList(
        new SetPriceCommand("setprice", "Stel een plotprijs in", "<plotprijs>", "bplots.setprice"),
        new QuickSellCommand("quicksell", "Quicksell je plot aan de gemeente", "", ""),
        new BuyCommand("buy", "Koop een plot", "", ""),
        new TransferCommand("transfer", "Zet een plot over op een andere speler", "<speler>", ""),
        new SellCommand("sell", "Verkoop een plot", "<prijs>", ""),
        new StopSaleCommand("stopsale", "Haal een plot uit de verkoop", "", ""),
        new PlotInfoCommand("info", "Krijg informatie over een plot", "", ""),

        new SaleMenuCommand("salemenu", "Een lijst met welke plots te koop zijn", "", ""),
        new HelpCommand(this, "help", "Een lijst met commands", "", ""),
        new ReloadCommand("reload", "Reload de config files", "", "bplots.reload"),

        new AddOwnerCommand("addowner", "Voeg owners toe aan een plot", "<speler>", "bplots.addowner"),
        new RemoveOwnerCommand("removeowner", "Verwijder owners van een plot", "<speler>", "bplots.removeowner"),
        new AddMemberCommand("addmember", "Voeg members toe aan een plot", "<speler>", ""),
        new RemoveMemberCommand("removemember", "Verwijder members van een plot", "<speler>", "")
    );

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Messages messages = Main.getMessages();

        try {
            SubCommand subCommand = subCommands.stream()
                    .filter(sub -> sub.getName().equalsIgnoreCase(args.length == 0 ? "" : args[0]))
                    .findFirst().orElse(null);

            if (!(sender instanceof Player)) {
                sender.sendMessage(messages.get(Message.NO_CONSOLE));
            } else if (subCommand == null) {
                sender.sendMessage(messages.get(Message.UNKNOWN_COMMAND, Placeholder.of("Command", label)));
            } else if (subCommand.getPermission() != null && subCommand.getPermission().length() > 0 &&
                    !sender.hasPermission(subCommand.getPermission())) {
                sender.sendMessage(messages.get(Message.NO_PERMISSION));
            } else if (!subCommand.onCommand((Player) sender, label, Arrays.copyOfRange(args, 1, args.length))) {
                    sender.sendMessage(messages.get(Message.INVALID_USAGE,
                            Placeholder.of("Usage", label + " " + subCommand.getName() + " " + subCommand.getUsage())));
            }
        } catch (Exception ex) {
            sender.sendMessage(messages.get(Message.UNKNOWN_ERROR));
            Bukkit.getLogger().warning("Er ging iets fout bij het uitvoeren van de command \"/" + label + " " +
                    String.join(" ", args) + "\" door " + sender.getName());
            ex.printStackTrace();
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) return Collections.emptyList();

        if (args.length == 1) {
            return getApplicableCompletions(args[0], subCommands.stream().map(SubCommand::getName).toArray(String[]::new));
        } else {
            SubCommand subCommand = subCommands.stream().filter(cmd -> cmd.getName().equalsIgnoreCase(args[0])).findFirst().orElse(null);
            if (subCommand != null) {
                return getApplicableCompletions(args[0],
                        subCommand.onTabComplete(Arrays.copyOfRange(args, 1, args.length)).toArray(new String[0]));
            }
        }

        return Collections.emptyList();
    }

    private List<String> getApplicableCompletions(String arg, String... completions) {
        return StringUtil.copyPartialMatches(arg.toLowerCase(), Arrays.asList(completions), new ArrayList<>());
    }
}
