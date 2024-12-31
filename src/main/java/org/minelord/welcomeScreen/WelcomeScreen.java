package org.minelord.welcomeScreen;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;

public final class WelcomeScreen extends JavaPlugin implements Listener {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {}

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        FileConfiguration config = getConfig();

        Bukkit.getRegionScheduler().runDelayed(this, player.getLocation(), (task) -> {

            String playerName = miniMessage.serialize(player.displayName());
            String serverName = config.getString("server_name");

            String language = player.getLocale().split("_")[0];


            String titleMessage = config.getString("messages." + language + ".title");
            String subtitleMessage = config.getString("messages." + language + ".subtitle");

            if (titleMessage == null || subtitleMessage == null) {
                titleMessage = config.getString("messages.en.title");
                subtitleMessage = config.getString("messages.en.subtitle");
            }

            titleMessage = titleMessage.replace("%player%", playerName).replace("%servername%", serverName);
            subtitleMessage = subtitleMessage.replace("%player%", playerName).replace("%servername%", serverName);

            Component title = miniMessage.deserialize(titleMessage);
            Component subtitle = miniMessage.deserialize(subtitleMessage);

            Title welcomeTitle = Title.title(
                    title,
                    subtitle,
                    Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(5), Duration.ofSeconds(1))
            );

            player.showTitle(welcomeTitle);

        }, 20L);
    }
}



