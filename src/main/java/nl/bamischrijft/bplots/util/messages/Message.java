package nl.bamischrijft.bplots.util.messages;

public enum Message {
    NO_CONSOLE("error.no-console", "&cAlleen spelers kunnen dit command gebruiken"),
    NO_PERMISSION("error.no-permission", "&cJe hebt geen permissies om dit te doen"),
    UNKNOWN_ERROR("error.unknown", "&cEr ging iets fout!"),
    INVALID_USAGE("error.invalid-usage", "&cGebruik &4/<Usage>"),
    UNKNOWN_COMMAND("error.unknown-subcommand", "&cOnbekend command, gebruik &4/<Command> help &cvoor een lijst met commands"),
    UNKNOWN_PLAYER("error.unknown-player", "&cSpeler &4<Player> &cis niet gevonden"),
    NOT_AN_INTEGER("error.not-a-number", "&4<Input> &cis geen geldig getal"),
    NOT_ENOUGH_MONEY("error.insufficient-balance", "&cJe hebt niet genoeg geld op je rekening staan om dit te doen"),
    NOT_ON_A_PLOT("error.not-on-a-plot", "&cJe staat niet op een plot!"),
    NOT_AN_OWNER("error.not-an-owner", "&cJe moet eigenaar zijn om dit te kunnen doen"),

    PLOT_NO_PRICE_SET("plot.no-price-set", "&cDit plot heeft nog geen prijs"),

    PLOT_SET_PRICE_SUCCESS("plot.set-price.success", "&3Je hebt de plotprijs van dit plot op &b€<Price>,- &3gezet"),
    PLOT_SET_PRICE_NOT_POSITIVE("plot.set-price.not-positive", "&cJe kan de plotprijs niet op een negatief getal zetten"),

    PLOT_QUICKSELL_CONFIRM("plot.quicksell.confirm", "&3Weet je zeker dat je dit plot voor " +
            "&b€<QuickSellPrice>,- &3wilt verkopen? Dat is &b<QuickSellPercentage>% &3van de waarde &8(&b€<Price>,-&8)&3. Typ dan &b/<Command> quicksell confirm"),
    PLOT_CANNOT_QUICKSELL_IF_FOR_SALE("plot.quicksell.cannot-quicksell-if-for-sale", "&cJe kan een plot niet quicksellen als hij te koop staat"),
    PLOT_QUICKSELL_SUCCESS("plot.quicksell.success", "&3Je hebt dit plot verkocht aan de gemeente voor &b€<QuickSellPrice>,-"),

    PLOT_BUY_TO_MUCH_PLOTS("plot.buy.max-plots", "&cJe zit al op de maximale aantal plots!"),
    PLOT_BUY_CONFIRM("plot.buy.confirm", "&3Weet je zeker dat je dit plot wilt kopen voor &b€<Price>,-? " +
            "&3Typ dan &b/<Command> buy confirm"),
    PLOT_BUY_SUCCESS("plot.buy.success", "&3Je hebt dit plot &8(&b<PlotName>&8) &3gekocht voor &b€<Price>,-"),
    PLOT_CANT_BUY_OWN_PLOT("plot.buy.cant-buy-own-plot", "&cJe kan je eigen plot niet kopen"),
    PLOT_NOT_FOR_SALE("plot.buy.not-for-sale", "&cDit plot is niet te koop"),

    PLOT_TRANSFER_CONFIRM("plot.transfer.confirm", "&cWeet je zeker dat je dit plot wilt overzetten" +
            " op <NewOwner>? Doe dan &4/<Command> transfer <NewOwner> confirm"),
    PLOT_TRANSFER_SUCCESS("plot.transfer.success", "&3Je hebt het plot &b<PlotName> &3overgezet naar &b<NewOwner>"),

    PLOT_ADD_MEMBER("plot.addmember", "&3Je hebt &b<NewMember> &3toegevoegd aan plot &b<PlotName>"),
    PLOT_ADD_OWNER("plot.addowner", "&3Je hebt &b<NewOwner> &3toegevoegd aan plot &b<PlotName>"),
    PLOT_REMOVE_MEMBER("plot.removemember", "&3Je hebt &b<OldMember> &3verwijderd van plot &b<PlotName>"),
    PLOT_REMOVE_OWNER("plot.removeowner", "&3Je hebt &b<OldOwner> &3verwijderd van plot &b<PlotName>"),

    PLOT_SELL_PRICE_NOT_POSITIVE("plot.sell.not-positive", "&cJe kan je plot niet voor een negatieve prijs verkopen"),
    PLOT_ALREADY_FOR_SALE("plot.sell.already-for-sale", "&cDit plot is al te koop, gebruik &4/<Command> stopsale &com hem uit de verkoop te halen"),
    PLOT_SELL_SUCCESS("plot.sell.success", "&aDit plot staat nu te koop voor $<Price>"),
    PLOT_MAX_SALES("plot.sell.max-sales", "&cJe hebt al &4<SaleCount> &cplots te koop staan, haal er eerst een uit de verkoop voordat je er weer een te koop kan zetten"),

    PLOT_REMOVED_FROM_SALE("plot.stopsale.removed-from-sale", "&aPlot is niet meer te koop"),
    PLOT_NOT_YET_FOR_SALE("plot.stopsale.not-yet-for-sale", "&cPlot is nog niet te koop. Gebruik &4/<Command> sell <prijs> &com hem te koop te zetten"),

    NO_PLOTS_FOR_SALE("plot.plotmenu.no-plots-for-sale", "&cEr zijn geen plots te koop in deze wereld"),

    HELP_COMMAND_COMMAND_FORMAT("plot.help.command-format", "&3/<Command> &b<SubCommand> &7- &3<Description>"),

    RELOAD_SUCCESS("plot.reload.success", "&3Config herladen")
    ;

    private final String path, defaultMessage;
    Message(String path, String defaultMessage) {
        this.path = path;
        this.defaultMessage = defaultMessage;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public String getPath() {
        return path;
    }
}
