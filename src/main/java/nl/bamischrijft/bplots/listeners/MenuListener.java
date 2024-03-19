package nl.bamischrijft.bplots.listeners;

import nl.bamischrijft.bplots.util.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
/**
 *
 * bPlots
 * © 2021 BamiSchijf.me (</BamiSchijf>#2826)
 *
 * Raak dit niet aan!
 *
 * © Copyrighted
 */
public class MenuListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof Menu) {
            Menu menu = (Menu) event.getInventory().getHolder();
            if (event.getRawSlot() > event.getInventory().getSize()) return;
            if (event.getCurrentItem() == null) return;

            menu.onClick((Player) event.getWhoClicked(), event.getCurrentItem(), event.getClick());
            event.setCancelled(true);
        }
    }
}
