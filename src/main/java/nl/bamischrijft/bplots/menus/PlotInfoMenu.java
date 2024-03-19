package nl.bamischrijft.bplots.menus;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nl.bamischrijft.bplots.Main;
import nl.bamischrijft.bplots.util.ItemBuilder;
import nl.bamischrijft.bplots.util.WorldGuardUtil;
import nl.bamischrijft.bplots.util.Menu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
/**
 *
 * bPlots
 * © 2021 BamiSchijf.me (</BamiSchijf>#2826)
 *
 * Raak dit niet aan!
 *
 * © Copyrighted
 */
public class PlotInfoMenu implements Menu {
    private static Integer quickSellPercentage;
    private static String title, itemTitleColor, itemTextColor;
    private final Player player;
    private final ProtectedRegion region;

    public PlotInfoMenu(Player player, ProtectedRegion region) {
        this.player = player;
        this.region = region;


        FileConfiguration config = Main.getPlugin().getConfig();
        if (title == null) {
            title = config.getString("plotinfo-menu.title", "&aPlotInfo");
            title = ChatColor.translateAlternateColorCodes('&', title);
        }

        if (itemTitleColor == null) {
            itemTitleColor = config.getString("plotinfo-menu.item-title-color", "&6");
            itemTitleColor = ChatColor.translateAlternateColorCodes('&', itemTitleColor);
        }

        if (itemTextColor == null) {
            itemTextColor = config.getString("plotinfo-menu.item-text-color", "&8");
            itemTextColor = ChatColor.translateAlternateColorCodes('&', itemTextColor);
        }

        if (quickSellPercentage == null) {
            quickSellPercentage = Main.getPlugin().getConfig().getInt("settings.quicksell.percentage", 80);
        }
    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 9*3, title);

        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).durability((short)15).build());
        }

        ItemStack plot = new ItemBuilder(Material.BOOK)
                .name(itemTitleColor + "Plotnaam")
                .lore(itemTextColor + region.getId())
                .build();

        String[] ownerNames = region.getOwners().getUniqueIds().stream()
                .map(Bukkit::getOfflinePlayer)
                .filter(offlinePlayer -> offlinePlayer.hasPlayedBefore() || offlinePlayer.isOnline())
                .map(OfflinePlayer::getName)
                .map(owner -> itemTextColor + owner).toArray(String[]::new);
        String[] memberNames = region.getMembers().getUniqueIds().stream()
                .map(Bukkit::getOfflinePlayer)
                .filter(offlinePlayer -> offlinePlayer.hasPlayedBefore() || offlinePlayer.isOnline())
                .map(OfflinePlayer::getName)
                .map(member -> itemTextColor + member).toArray(String[]::new);

        ItemStack owners = new ItemBuilder(Material.ENDER_CHEST)
                .name(itemTitleColor + "Owners")
                .lore(ownerNames.length == 0 ? new String[]{itemTextColor + "Geen owners"} : ownerNames)
                .build();

        ItemStack members = new ItemBuilder(Material.CHEST)
                .name(itemTitleColor + "Members")
                .lore(memberNames.length == 0 ? new String[]{itemTextColor + "Geen members"} : memberNames)
                .build();

        inventory.setItem(11, plot);
        inventory.setItem(13, owners);
        inventory.setItem(14, members);

        Long price = region.getFlag(WorldGuardUtil.PLOT_PRICE);

        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        if (!region.isMember(localPlayer) && !region.isOwner(localPlayer)) {
            boolean forSale = ownerNames.length == 0 && price != null;

            ItemStack buy = new ItemBuilder(forSale ? Material.GOLD_INGOT : Material.BARRIER)
                    .name(itemTitleColor + "Buy")
                    .lore(itemTextColor + (forSale ? "Klik om dit plot te kopen voor " + itemTitleColor + "€" + price + ",-" : "Dit plot is niet te koop"))
                    .build();

            inventory.setItem(15, buy);
        }

        if (region.isOwner(localPlayer) && price != null) {
            ItemStack buy = new ItemBuilder(Material.ITEM_FRAME)
                    .name(itemTitleColor + "QuickSell")
                    .lore(itemTextColor + "Verkoop dit plot voor " + itemTitleColor + "€" + (int) Math.floor(price * (quickSellPercentage / 100.0)) + ",-")
                    .build();

            inventory.setItem(15, buy);
        }


        return inventory;
    }

    @Override
    public void onClick(Player player, ItemStack item, ClickType click) {
        switch (item.getType()) {
            case ITEM_FRAME:
                player.chat("/plot quicksell");
                break;
            case GOLD_INGOT:
                player.chat("/plot buy");
                break;
            default:
                return;
        }

        player.closeInventory();
    }
}
