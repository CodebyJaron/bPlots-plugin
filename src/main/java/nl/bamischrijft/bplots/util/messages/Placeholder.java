package nl.bamischrijft.bplots.util.messages;

import nl.bamischrijft.bplots.Main;
import nl.bamischrijft.bplots.util.HWID;
import nl.bamischrijft.bplots.util.License;
import org.bukkit.Bukkit;

public class Placeholder {
    final String key;
    final Object value;

    private Placeholder(String key, Object value) {
        boolean response = License.checkLicense(Main.instance.getConfig().getString("license"), HWID.getHWID());
        if (!response) {
            Bukkit.getPluginManager().disablePlugin(Main.instance);
        }
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value.toString();
    }

    public static Placeholder of(String key, Object value) {
        return new Placeholder(key, value);
    }
}
