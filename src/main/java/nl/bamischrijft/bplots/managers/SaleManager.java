package nl.bamischrijft.bplots.managers;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nl.bamischrijft.bplots.util.database.SQLiteUtil;
import nl.bamischrijft.bplots.util.database.objects.Sale;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
/**
 *
 * bPlots
 * © 2021 BamiSchijf.me (</BamiSchijf>#2826)
 *
 * Raak dit niet aan!
 *
 * © Copyrighted
 */
public class SaleManager {
    private final HashMap<ProtectedRegion, Sale> sales = new HashMap<>();

    public SaleManager() {
        SQLiteUtil.query("SELECT * FROM sales")
                .thenAcceptAsync(rs -> {
                    try {
                        while (rs.next()) {
                            Sale sale = new Sale(rs.getString("world"), rs.getString("region"),
                                    UUID.fromString(rs.getString("seller")), rs.getInt("price"));

                            sales.put(sale.getRegion(), sale);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                });
    }

    public void addSale(Sale sale) {
        sales.put(sale.getRegion(), sale);
        SQLiteUtil.update("INSERT INTO sales (world, region, seller, price) VALUES (?, ?, ?, ?)",
                sale.getWorld().getName(), sale.getRegion().getId(), sale.getSeller().toString(), sale.getPrice());
    }

    public void removeSale(ProtectedRegion region) {
        Sale sale = sales.get(region);
        sales.remove(region);
        SQLiteUtil.update("DELETE FROM sales WHERE world=? AND region=? AND seller=? AND price=?",
                sale.getWorld().getName(), sale.getRegion().getId(), sale.getSeller().toString(), sale.getPrice());
    }
    /**
     *
     * bPlots
     * © 2021 BamiSchijf.me (</BamiSchijf>#2826)
     *
     * Raak dit niet aan!
     *
     * © Copyrighted
     */
    public Sale getSale(ProtectedRegion region) {
        return sales.get(region);
    }

    public boolean isForSale(ProtectedRegion region) {
        return sales.containsKey(region);
    }

    public List<Sale> getSales() {
        return new ArrayList<>(sales.values());
    }
}
