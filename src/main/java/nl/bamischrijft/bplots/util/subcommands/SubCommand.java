package nl.bamischrijft.bplots.util.subcommands;

import nl.bamischrijft.bplots.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public abstract class SubCommand {
    String name, description, usage, permission;

    public SubCommand(String name, String defaultDescription, String defaultUsage, String defaultPermission) {
        FileConfiguration config = Main.getPlugin().getConfig();

        this.name = name;

        String prefix = "commands.plot.subcommands." + name + ".";

        description = config.getString(prefix + "description");
        if (description == null) {
            config.set(prefix + "description", defaultDescription);
            this.description = defaultDescription;
        }

        usage = config.getString(prefix + "usage");
        if (usage == null) {
            config.set(prefix + "usage", defaultUsage);
            this.usage = defaultUsage;
        }

        permission = config.getString(prefix + "permission");
        if (permission == null) {
            config.set(prefix + "permission", defaultPermission);
            this.permission = defaultPermission;
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public String getPermission() {
        return permission;
    }

    public abstract boolean onCommand(Player player, String label, String[] args) throws Exception;

    public List<String> onTabComplete(String[] args) {
        return Collections.emptyList();
    }
}
