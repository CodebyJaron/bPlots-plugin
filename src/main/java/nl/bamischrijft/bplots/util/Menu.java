package nl.bamischrijft.bplots.util;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public interface Menu extends InventoryHolder {
    void onClick(Player player, ItemStack item, ClickType click);
}