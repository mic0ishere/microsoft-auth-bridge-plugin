package me.mic0.microsoftauthbridge;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MicrosoftAuthBridge extends JavaPlugin implements Listener {
    public FileConfiguration config = getConfig();
    public MongoCollection<Document> collection;
    public MongoClient mongoClient;

    @Override
    public void onEnable() {
        Logger mongoLogger = Logger.getLogger("com.mongodb");
        mongoLogger.setLevel(Level.WARNING);

        config.addDefault("mongoURL", "<mongoURL>");
        config.addDefault("bypassPlayers", List.of());
        config.addDefault("kickMessage", "You are not allowed to join this server.");

        config.options().copyDefaults(true);
        saveConfig();

        String mongoURL = config.getString("mongoURL");
        if (mongoURL == null || !mongoURL.startsWith("mongodb")) {
            getLogger().severe("Please enter correct MongoDB url in the config.yml file.");
            return;
        }

        this.mongoClient = MongoClients.create(mongoURL);
        collection = mongoClient.getDatabase("microsoft-auth-bridge").getCollection("users");

        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled!");
    }

    @EventHandler
    public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
        String playerName = event.getName().toLowerCase(Locale.ROOT);

        List<String> bypassPlayers = config.getStringList("bypassPlayers").stream().map(String::toLowerCase).toList();

        if (bypassPlayers.contains(playerName)) {
            getLogger().info("Allowed player " + playerName + " to connect (reason: player on bypass list)");
            event.allow();
            return;
        }

        Document document = collection.find(new Document("username", playerName)).first();

        if (document == null) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, config.getString("kickMessage"));
            getLogger().info("Prevented player " + playerName + " from connecting");
        } else {
            event.allow();
            getLogger().info("Allowed player " + playerName + " to connect (reason: database entry)");
        }
    }
}
