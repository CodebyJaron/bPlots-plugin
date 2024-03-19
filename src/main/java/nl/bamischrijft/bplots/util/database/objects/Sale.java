package nl.bamischrijft.bplots.util.database.objects;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nl.bamischrijft.bplots.util.WorldGuardUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;
import java.util.UUID;

public class Sale {
    ProtectedRegion region;
    World world;
    UUID seller;
    long price;

    public Sale(String worldName, String regionName, UUID seller, long price) {
        World world = Bukkit.getWorld(worldName);;

        ProtectedRegion region = null;
        try {
            region = WorldGuardUtil.getRegionManager(world).getRegion(regionName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.world = world;
        this.region = region;
        this.seller = seller;
        this.price = price;
    }

    public Sale(World world, ProtectedRegion region, UUID seller, long price) {
        this.world = world;
        this.region = region;
        this.seller = seller;
        this.price = price;
    }

    public ProtectedRegion getRegion() {
        return region;
    }

    public World getWorld() {
        return world;
    }

    public UUID getSeller() {
        return seller;
    }

    public long getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return price == sale.price && Objects.equals(world, sale.world) &&
                Objects.equals(region, sale.region) && Objects.equals(seller, sale.seller);
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, world, seller, price);
    }

    @Override
    public String toString() {
        return "Sale{" +
                "region=" + region +
                ", world=" + world +
                ", seller=" + seller +
                ", price=" + price +
                '}';
    }
}