package nl.bamischrijft.bplots.menus;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.milkbowl.vault.economy.Economy;
import nl.bamischrijft.bplots.Main;
import nl.bamischrijft.bplots.util.messages.Message;
import nl.bamischrijft.bplots.util.messages.Placeholder;
import nl.bamischrijft.bplots.util.ItemBuilder;
import nl.bamischrijft.bplots.util.Menu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
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
public class PlotBuyMenu implements Menu {
    private final long price;
    private final ProtectedRegion region;
    private final OfflinePlayer seller;

    public PlotBuyMenu(long price, ProtectedRegion region, OfflinePlayer seller) {
        this.price = price;
        this.region = region;
        this.seller = seller;
    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 27, ChatColor.GREEN + "Wil je dit plot kopen voor $" + price + "?");

        ItemStack confirmItem = new ItemBuilder(Material.WOOL)
                .durability((short) 13)
                .name(ChatColor.GREEN + "Ja")
                .build();

        ItemStack cancelItem = new ItemBuilder(Material.WOOL)
                .durability((short) 14)
                .name(ChatColor.RED + "Nee")
                .build();

        inventory.setItem(11, confirmItem);
        inventory.setItem(15, cancelItem);

        return inventory;
    }

    @Override
    public void onClick(Player player, ItemStack item, ClickType click) {
        switch (item.getDurability()) {
            case 14:
                player.closeInventory();
                break;
            case 13:
                player.closeInventory();

                if (!Main.getSaleManager().isForSale(region)) {
                    player.sendMessage(Main.getMessages().get(Message.PLOT_NOT_FOR_SALE));
                    return;
                }

                if (region.isOwner(WorldGuardPlugin.inst().wrapPlayer(player))) {
                    player.sendMessage(Main.getMessages().get(Message.PLOT_CANT_BUY_OWN_PLOT));
                    return;
                }

                Economy economy = Main.getEconomy();
                economy.withdrawPlayer(player, price);
                economy.depositPlayer(seller, price);

                region.getOwners().clear();
                region.getMembers().clear();
                region.getOwners().addPlayer(player.getUniqueId());
                Main.getSaleManager().removeSale(region);

                player.sendMessage(Main.getMessages().get(Message.PLOT_BUY_SUCCESS,
                        Placeholder.of("PlotName", region.getId()), Placeholder.of("Price", price)));
                break;
        }
    }
}
