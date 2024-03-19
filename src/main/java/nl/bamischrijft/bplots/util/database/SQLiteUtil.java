package nl.bamischrijft.bplots.util.database;

import com.sun.rowset.CachedRowSetImpl;
import nl.bamischrijft.bplots.Main;
import nl.bamischrijft.bplots.util.HWID;
import nl.bamischrijft.bplots.util.License;
import org.bukkit.Bukkit;

import javax.sql.rowset.CachedRowSet;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.concurrent.CompletableFuture;

public class SQLiteUtil {
    private static String DATABASE_PATH;

    public static boolean initDatabase() {
        Main.getPlugin().getDataFolder().mkdirs();

        DATABASE_PATH = Main.getPlugin().getDataFolder().getAbsolutePath() + File.separator + "data.db";
        if (!Files.exists(Paths.get(DATABASE_PATH))) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH);
                PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS sales(" +
                        "world TEXT NOT NULL, " +
                        "region TEXT UNIQUE NOT NULL, " +
                        "seller VARCHAR(50) NOT NULL, " +
                        "price INT NOT NULL);");
                statement.execute();
                statement.close();
                Bukkit.getLogger().info("SQLite bestand aangemaakt");
                return true;
            } catch (SQLException e) {
                Bukkit.getLogger().severe("Er ging iets fout met het aanmaken van de database. De plugin wordt uitgeschakeld.");
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public static CompletableFuture<Boolean> update(String sql, Object... params) {
        return CompletableFuture.supplyAsync(() -> {
            Connection connection = null;
            PreparedStatement statement = null;
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH);
                statement = connection.prepareStatement(sql);

                for (int i = 0; i < params.length; i++) statement.setObject(i + 1, params[i]);

                statement.executeUpdate();
                return true;
            } catch (SQLException|ClassNotFoundException e) {
                System.out.println("Error with SQL statement: " + sql);
                e.printStackTrace();
                return false;
            } finally {
                try {
                    if (statement != null) statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static CompletableFuture<CachedRowSet> query(String sql, Object... params) {
        return CompletableFuture.supplyAsync(() -> {
            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH);
                statement = connection.prepareStatement(sql);
                for (int i = 0; i < params.length; i++)
                    statement.setString(i + 1, params[i].toString());

                resultSet = statement.executeQuery();
                CachedRowSet cachedRowSet = new CachedRowSetImpl();
                cachedRowSet.populate(resultSet);

                return cachedRowSet;
            } catch (SQLException | ClassNotFoundException e) {
                return null;
            } finally {
                try {
                    if (resultSet != null) resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    if (statement != null) statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
