package nl.bamischrijft.bplots.util;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.FlagContext;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class WorldGuardUtil {
    public static Flag<Long> PLOT_PRICE = new Flag<Long>("bplots-plot-price") {
        @Override
        public Long parseInput(FlagContext flagContext) throws InvalidFlagFormat {
            try {
                return Long.parseLong(flagContext.getUserInput());
            } catch (NumberFormatException var2) {
                throw new InvalidFlagFormat("Not a number: " + flagContext.getUserInput());
            }
        }

        public Long unmarshal(Object o) {
            if (o instanceof Long) {
                return (Long) o;
            } else return o instanceof Number ? ((Number) o).longValue() : null;
        }

        @Override
        public Object marshal(Long aLong) {
            return aLong;
        }
    };

    private static WorldGuardPlugin worldGuardPlugin = null;

    public static WorldGuardPlugin getWorldGuard() {
        if (worldGuardPlugin == null) {
            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
            worldGuardPlugin = plugin instanceof WorldGuardPlugin ? (WorldGuardPlugin) plugin : null;
        }

        return worldGuardPlugin;
    }

    public static RegionManager getRegionManager(World world) throws Exception {
        if (usesWorldGuard7()) {
            Class worldguard = Class.forName("com.sk89q.worldguard.WorldGuard");
            Object worldguardInstance = worldguard.getDeclaredMethod("getInstance").invoke(null);
            Object platform = worldguardInstance.getClass().getDeclaredMethod("getPlatform").invoke(worldguardInstance);
            Object regionContainer = platform.getClass().getDeclaredMethod("getRegionContainer").invoke(platform);
            return (RegionManager) regionContainer.getClass().getSuperclass()
                    .getMethod("get", com.sk89q.worldedit.world.World.class)
                    .invoke(regionContainer, new BukkitWorld(world));
        } else return getWorldGuard().getRegionManager(world);
    }

    public static ProtectedRegion getRegion(Location location) throws Exception {
        ArrayList<ProtectedRegion> regions;
        if (usesWorldGuard7()) {
            RegionManager regionManager = getRegionManager(location.getWorld());
            Class blockVectorClass = Class.forName("com.sk89q.worldedit.math.BlockVector3");

            Method at = blockVectorClass.getDeclaredMethod("at", Double.TYPE, Double.TYPE, Double.TYPE);
            Object blockvector = at.invoke(null, location.getX(), location.getY(), location.getZ());

            Method getApplicableRegions = regionManager.getClass().getMethod("getApplicableRegions", blockVectorClass);
            Object applicableRegionsSet =  getApplicableRegions.invoke(regionManager, blockvector);
            Method getRegions = applicableRegionsSet.getClass().getMethod("getRegions");

            regions = new ArrayList<>((HashSet<ProtectedRegion>) getRegions.invoke(applicableRegionsSet));
        } else regions = new ArrayList<>(getRegionManager(location.getWorld()).getApplicableRegions(location).getRegions());

        regions.sort((region1, region2) -> {
            if (region1.getPriority() == region2.getPriority()) {
                return 0;
            } else if (region1.getPriority() < region2.getPriority()) {
                return 1;
            } else if (region1.getPriority() > region2.getPriority()) {
                return -1;
            } else return 9999;
        });

        return regions.size() == 0 ? null : regions.get(0);
    }

    public static int getRegionAmount(Player player) throws Exception {
        return getRegionManager(player.getWorld()).getRegionCountOfPlayer(getWorldGuard().wrapPlayer(player));
    }

    private static boolean usesWorldGuard7() {
        return getWorldGuard().getDescription().getVersion().startsWith("7.");
    }
}
