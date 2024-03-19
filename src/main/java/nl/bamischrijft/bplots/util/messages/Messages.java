package nl.bamischrijft.bplots.util.messages;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class Messages {
    private final HashMap<Message, String> cache = new HashMap<>();

    public Messages(Plugin plugin) {
        FileConfiguration config = plugin.getConfig();
        for (Message message : Message.values()) {
            String text = config.getString("messages." + message.getPath());
            if (text == null) {
                config.set("messages." + message.getPath(), message.getDefaultMessage());
                text = message.getDefaultMessage();
            }

            cache.put(message, ChatColor.translateAlternateColorCodes('&', text));
        }

        plugin.saveConfig();
    }

    public String get(Message message, Placeholder... placeholders) {
        String formattedMessage = cache.get(message);

        for (Placeholder placeholder : placeholders)
            formattedMessage = formattedMessage.replace("<" + placeholder.getKey() + ">", placeholder.getValue());

        return formattedMessage;
    }
}
