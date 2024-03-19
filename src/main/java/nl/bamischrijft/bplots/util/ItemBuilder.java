package nl.bamischrijft.bplots.util;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ItemBuilder - API Class to create a {@link org.bukkit.inventory.ItemStack} with just one line of Code
 * @version 1.8
 * @author Acquized
 * @contributor Kev575
 */
public class ItemBuilder {

    private ItemStack item;
    private ItemMeta meta;
    private Material material = Material.STONE;
    private int amount = 1;
    private MaterialData data;
    private short damage = 0;
    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    private String displayname;
    private List<String> lore = new ArrayList<>();
    private List<ItemFlag> flags = new ArrayList<>();

    private boolean andSymbol = true;

    /** Initalizes the ItemBuilder with {@link org.bukkit.Material} */
    public ItemBuilder(Material material) {
        if(material == null) material = Material.AIR;
        this.item = new ItemStack(material);
        this.material = material;
    }

    /**
     * Initalizes the ItemBuilder with an already existing
     * @deprecated Use the already initalized {@code ItemBuilder} Instance to improve performance
     */
    @Deprecated
    public ItemBuilder(ItemBuilder builder) {
        Validate.notNull(builder, "The ItemBuilder is null.");
        this.item = builder.item;
        this.meta = builder.meta;
        this.material = builder.material;
        this.amount = builder.amount;
        this.damage = builder.damage;
        this.data = builder.data;
        this.enchantments = builder.enchantments;
        this.displayname = builder.displayname;
        this.lore = builder.lore;
        this.flags = builder.flags;
    }

    /**
     * Sets the Damage of the ItemStack
     * @param damage Damage for the ItemStack
     * @deprecated Use {@code ItemBuilder#durability}
     */
    @Deprecated
    public ItemBuilder damage(short damage) {
        this.damage = damage;
        return this;
    }

    /**
     * Sets the Durability (Damage) of the ItemStack
     * @param damage Damage for the ItemStack
     */
    public ItemBuilder durability(short damage) {
        this.damage = damage;
        return this;
    }

    /**
     * Sets the Displayname of the ItemStack
     * @param displayname Displayname for the ItemStack
     */
    public ItemBuilder name(String displayname) {
        Validate.notNull(displayname, "The Displayname is null.");
        this.displayname = andSymbol ? ChatColor.translateAlternateColorCodes('&', displayname) : displayname;
        return this;
    }

    /**
     * Adds a Line to the Lore of the ItemStack
     * @param line Line of the Lore for the ItemStack
     */
    public ItemBuilder lore(String line) {
        Validate.notNull(line, "The Line is null.");
        lore.add(andSymbol ? ChatColor.translateAlternateColorCodes('&', line) : line);
        return this;
    }

    /**
     * Adds one or more Lines to the Lore of the ItemStack
     * @param lines One or more Strings for the ItemStack Lore
     */
    public ItemBuilder lore(String... lines) {
        Validate.notNull(lines, "The Lines are null.");
        for (String line : lines) {
            lore(andSymbol ? ChatColor.translateAlternateColorCodes('&', line) : line);
        }
        return this;
    }

    /** Converts the ItemBuilder to a {@link org.bukkit.inventory.ItemStack} */
    public ItemStack build() {
        item.setType(material);
        item.setAmount(amount);
        item.setDurability(damage);
        meta = item.getItemMeta();

        if (data != null) item.setData(data);

        if (enchantments.size() > 0) item.addUnsafeEnchantments(enchantments);

        if (displayname != null) meta.setDisplayName(displayname);

        if (lore.size() > 0) meta.setLore(lore);

        if (flags.size() > 0) {
            for (ItemFlag f : flags) meta.addItemFlags(f);
        }

        item.setItemMeta(meta);

        return item;
    }
}
