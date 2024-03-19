package nl.bamischrijft.bplots.menus;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nl.bamischrijft.bplots.Main;
import nl.bamischrijft.bplots.util.*;
import nl.bamischrijft.bplots.util.database.objects.Sale;
import nl.bamischrijft.bplots.util.messages.Message;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PlotSalesMenu implements Menu {
    private static String title, itemTitleColor, itemPriceColor, itemSellerColor;

    private final int index;
    private final Player player;

    public PlotSalesMenu(int index, Player player) {
        this.index = index;
        this.player = player;

        FileConfiguration config = Main.getPlugin().getConfig();
        if (title == null) {
            title = config.getString("plot-sales-menu.title", "&aPlotinfo");
            title = ChatColor.translateAlternateColorCodes('&', title);
        }

        if (itemTitleColor == null) {
            itemTitleColor = config.getString("plot-sales-menu.item-title-color", "&bPlot: ");
            itemTitleColor = ChatColor.translateAlternateColorCodes('&', itemTitleColor);
        }

        if (itemPriceColor == null) {
            itemPriceColor = config.getString("plot-sales-menu.item-price-color", "&3Prijs: ");
            itemPriceColor = ChatColor.translateAlternateColorCodes('&', itemPriceColor);
        }

        if (itemSellerColor == null) {
            itemSellerColor = config.getString("plot-sales-menu.item-seller-color", "&bVerkoper: ");
            itemSellerColor = ChatColor.translateAlternateColorCodes('&', itemSellerColor);
        }
    }

    @Override
    public Inventory getInventory() {
        List<Sale> sales = Main.getSaleManager().getSales().stream()
                .filter(sale -> sale.getWorld() == player.getWorld()).collect(Collectors.toList());

        if (sales.size() == 0) {
            player.sendMessage(Main.getMessages().get(Message.NO_PLOTS_FOR_SALE));
            return null;
        }

        int salesToShowCount = Math.min(45, sales.size());
        int inventorySize = (int) Math.ceil(salesToShowCount / 9.0) * 9 + 9;

        Inventory inventory = Bukkit.createInventory(this, inventorySize, title);

        inventory.addItem(sales.stream()
                .skip(this.index * 45L).limit(45)
                .map(sale -> {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(sale.getSeller());
                    if (player == null) return null;
                    ItemStack item  = new ItemBuilder(Material.CHEST)
                            .name(itemTitleColor + sale.getRegion().getId())
                            .lore(itemPriceColor + sale.getPrice(), itemSellerColor + player.getName())
                            .build();
                    return item;
                }).filter(Objects::nonNull).toArray(ItemStack[]::new));

        inventory.setItem(inventorySize - 5, new ItemBuilder(Material.BARRIER).name(ChatColor.RED + "Sluit het menu af").build());

        if (index > 0) {
            inventory.setItem(inventorySize - 6, new ItemBuilder(Material.SPECTRAL_ARROW)
                    .name(ChatColor.GOLD + "Ga naar de vorige pagina").build());
        }

        if (sales.size() > 45) {
            inventory.setItem(inventorySize - 4, new ItemBuilder(Material.SPECTRAL_ARROW)
                    .name(ChatColor.GOLD + "Ga naar de volgende pagina").build());
        }

        return inventory;
    }

    @Override
    public void onClick(Player player, ItemStack item, ClickType click) {
        switch (item.getType()) {
            case BARRIER:
                player.closeInventory();
                break;
            case CHEST:
                if (click.isLeftClick()) {
                    List<String> lore = item.getItemMeta().getLore();
                    long price = Long.parseLong(lore.get(0).substring(itemPriceColor.length()));
                    String seller = lore.get(1).substring(itemSellerColor.length());
                    String regionId = item.getItemMeta().getDisplayName().substring(itemTitleColor.length());
                    ProtectedRegion region = null;
                    try {
                        region = WorldGuardUtil.getRegionManager(player.getWorld()).getRegion(regionId);
                    } catch (Exception e) {
                        player.closeInventory();
                        player.sendMessage(Main.getMessages().get(Message.UNKNOWN_ERROR));
                        e.printStackTrace();
                        return;
                    }

                    PlotBuyMenu plotBuyMenu = new PlotBuyMenu(price, region, Util.getOfflinePlayer(seller));
                    player.closeInventory();
                    player.openInventory(plotBuyMenu.getInventory());
                } else {

                    String regionId = item.getItemMeta().getDisplayName().substring(itemTitleColor.length());
                    ProtectedRegion region = null;
                    try {
                        region = WorldGuardUtil.getRegionManager(player.getWorld()).getRegion(regionId);
                    } catch (Exception e) {
                        player.closeInventory();
                        player.sendMessage(Main.getMessages().get(Message.UNKNOWN_ERROR));
                        e.printStackTrace();
                        return;
                    }
                    double x = NBTEditor.getShort(item, "x");
                    double y = NBTEditor.getShort(item, "y");
                    double z = NBTEditor.getShort(item, "z");

//                    Main.getGPS().addPoint(regionId, new Location(Bukkit.getWorld("World"), x, y, z));
                }

                break;
            case SPECTRAL_ARROW:
                player.closeInventory();
                int newIndex = index;
                if (item.getItemMeta().getDisplayName().contains("vorige")) newIndex--;
                else newIndex++;

                Inventory inventory = new PlotSalesMenu(newIndex, player).getInventory();
                if (inventory == null) return;

                player.openInventory(inventory);
                break;
        }
    }
}
