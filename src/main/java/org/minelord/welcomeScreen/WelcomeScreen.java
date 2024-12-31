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
        // Cargar el archivo de configuración
        saveDefaultConfig(); // Si no existe el archivo, lo crea con el archivo predeterminado
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Lógica de apagado del plugin
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        FileConfiguration config = getConfig();

        Bukkit.getRegionScheduler().runDelayed(this, player.getLocation(), (task) -> {

            // Convertir el displayName del jugador a texto plano
            String playerName = miniMessage.serialize(player.displayName());
            String serverName = config.getString("server_name");

            // Obtener el idioma preferido del jugador (por ejemplo, en español 'es')
            String language = player.getLocale().split("_")[0]; // Obtiene el idioma preferido

            // Obtener el título y subtítulo correspondientes desde el archivo de configuración

            String titleMessage = config.getString("messages." + language + ".title");
            String subtitleMessage = config.getString("messages." + language + ".subtitle");

            // Si no se encuentra el idioma, se usa el idioma predeterminado (inglés)
            if (titleMessage == null || subtitleMessage == null) {
                titleMessage = config.getString("messages.en.title");
                subtitleMessage = config.getString("messages.en.subtitle");
            }

            // Reemplazar %player% y %servername% con los valores correspondientes
            titleMessage = titleMessage.replace("%player%", playerName).replace("%servername%", serverName);
            subtitleMessage = subtitleMessage.replace("%player%", playerName).replace("%servername%", serverName);

            // Crear el título y subtítulo usando Adventure
            Component title = miniMessage.deserialize(titleMessage);
            Component subtitle = miniMessage.deserialize(subtitleMessage);

            Title welcomeTitle = Title.title(
                    title,
                    subtitle,
                    Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(5), Duration.ofSeconds(1))
            );

            // Mostrar el título al jugador
            player.showTitle(welcomeTitle);

        }, 20L);
    }
}



