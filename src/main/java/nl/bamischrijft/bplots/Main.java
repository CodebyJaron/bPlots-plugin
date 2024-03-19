package nl.bamischrijft.bplots;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.milkbowl.vault.economy.Economy;
import nl.bamischrijft.bplots.commands.PlotCommand;
import nl.bamischrijft.bplots.util.HWID;
import nl.bamischrijft.bplots.util.License;
import nl.bamischrijft.bplots.util.WorldGuardUtil;
import nl.bamischrijft.bplots.commands.PlotInfoCommand;
import nl.bamischrijft.bplots.listeners.MenuListener;
import nl.bamischrijft.bplots.managers.SaleManager;
import nl.bamischrijft.bplots.util.database.SQLiteUtil;
import nl.bamischrijft.bplots.util.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private static Messages messages;
    private static Plugin plugin;
    private static Economy economy;
    private static SaleManager saleManager;
    public static Main instance;

    /**
     *
     * bPlots
     * © 2021 BamiSchijf.me (</BamiSchijf>#2826)
     *
     * Raak dit niet aan!
     *
     * © Copyrighted
     */

    @Override
    public void onLoad() {
        WorldGuardPlugin.inst().getFlagRegistry().register(WorldGuardUtil.PLOT_PRICE);
    }

    @Override
    public void onEnable() {
        plugin = this;
        instance = this;
        if (!setupEconomy()) {
            Bukkit.getLogger().warning("Couldn't connect with VaultAPI");
            setEnabled(false);
            return;
        }

        saveDefaultConfig();

        saveConfig();
        if(!new License(this, getConfig().getString("license"), "http://178.63.247.123:2000/api/client", "cd28fd6bb9b5407d7bbfe8f4ea560411d43ffa8c").verify()) {
            Bukkit.getPluginManager().disablePlugin(this);
            Bukkit.getScheduler().cancelTasks(this);
            return;
        }

        SQLiteUtil.initDatabase();
        messages = new Messages(this);
        saleManager = new SaleManager();

        PluginCommand trafficLightsCommand = getCommand("plot");
        trafficLightsCommand.setExecutor(new PlotCommand());
        trafficLightsCommand.setTabCompleter(new PlotCommand());
        getCommand("plotinfo").setExecutor(new PlotInfoCommand());

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new MenuListener(), this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;

        economy = rsp.getProvider();
        return economy != null;
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("----------------------------------");
        Bukkit.getConsoleSender().sendMessage("bPlots staat uit!");
        Bukkit.getConsoleSender().sendMessage("----------------------------------");
    }

    public static Messages getMessages() {
        return messages;
    }

    public static Plugin getPlugin() {
        return plugin;
    }


    public static Economy getEconomy() {
        return economy;
    }

    public static SaleManager getSaleManager() {
        return saleManager;
    }

    public static void reload() {
        getPlugin().reloadConfig();
        messages = new Messages(Main.getPlugin());
    }
}
