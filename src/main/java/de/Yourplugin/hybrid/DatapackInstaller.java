package de.yourplugin.hybrid;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Installer für das Custom-Datapack, das die maximale Höhe (8000 Blöcke) setzt.
 */
public class DatapackInstaller {

    private final JavaPlugin plugin;

    public DatapackInstaller(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void installDatapack() {
        File worldFolder = new File(Bukkit.getWorldContainer(), plugin.getConfig().getString("world-name", "world"));
        File datapacksFolder = new File(worldFolder, "datapacks/HybridSurvival");
        File dimensionFolder = new File(datapacksFolder, "data/minecraft/dimension_type");
        dimensionFolder.mkdirs();

        File overworldJson = new File(dimensionFolder, "overworld.json");

        String json = "{\n" +
                "  \"type\": \"minecraft:overworld\",\n" +
                "  \"height\": 8000,\n" +                // neue Höhe
                "  \"min_y\": -2048,\n" +
                "  \"logical_height\": 8000,\n" +        // neue logische Höhe
                "  \"coordinate_scale\": 1.0,\n" +
                "  \"ambient_light\": 0.0,\n" +
                "  \"fixed_time\": 6000,\n" +
                "  \"has_skylight\": true,\n" +
                "  \"has_ceiling\": false,\n" +
                "  \"ultrawarm\": false,\n" +
                "  \"natural\": true,\n" +
                "  \"piglin_safe\": false,\n" +
                "  \"bed_works\": true,\n" +
                "  \"respawn_anchor_works\": false,\n" +
                "  \"has_raids\": true,\n" +
                "  \"monster_spawn_light_level\": {\"type\": \"uniform\", \"value\": 0},\n" +
                "  \"monster_spawn_block_light_limit\": 0\n" +
                "}";

        try {
            if (!overworldJson.exists()) {
                overworldJson.createNewFile();
            }
            try (FileWriter writer = new FileWriter(overworldJson)) {
                writer.write(json);
            }
            plugin.getLogger().info("Custom Datapack mit Höhe 8000 installiert.");
        } catch (IOException e) {
            plugin.getLogger().severe("Fehler beim Schreiben des Datapacks: " + e.getMessage());
        }

        // pack.mcmeta hinzufügen
        File packMcmeta = new File(datapacksFolder, "pack.mcmeta");
        if (!packMcmeta.exists()) {
            String mcmeta = "{\n" +
                    "  \"pack\": {\n" +
                    "    \"pack_format\": 15,\n" +
                    "    \"description\": \"HybridSurvival Extended Height\"\n" +
                    "  }\n" +
                    "}";
            try {
                Files.writeString(Path.of(packMcmeta.toURI()), mcmeta);
            } catch (IOException e) {
                plugin.getLogger().severe("Fehler beim Schreiben der pack.mcmeta: " + e.getMessage());
            }
        }
    }
}