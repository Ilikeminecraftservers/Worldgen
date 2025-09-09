package de.example.worldgen;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DecorationManager {

    private final Plugin plugin;

    public DecorationManager(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Fügt ein Schematic an einer bestimmten Location ein.
     */
    public void pasteSchematic(Player player, byte[] schematicData, Location location) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (InputStream in = new ByteArrayInputStream(schematicData)) {
                ClipboardFormat format = ClipboardFormats.findByInputStream(in);

                if (format == null) {
                    player.sendMessage("§cUnbekanntes Schematic-Format!");
                    return;
                }

                try (ClipboardReader reader = format.getReader(in)) {
                    Clipboard clipboard = reader.read();
                    World adaptedWorld = new BukkitWorld(location.getWorld());
                    ClipboardHolder holder = new ClipboardHolder(clipboard);

                    // Hauptthread → tatsächliches Einfügen
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        try {
                            holder.createPaste(adaptedWorld)
                                    .to(com.sk89q.worldedit.math.BlockVector3.at(
                                            location.getBlockX(),
                                            location.getBlockY(),
                                            location.getBlockZ()
                                    ))
                                    .ignoreAirBlocks(true)
                                    .build();
                            player.sendMessage("§aSchematic erfolgreich eingefügt!");
                        } catch (WorldEditException e) {
                            player.sendMessage("§cFehler beim Einfügen des Schematics: " + e.getMessage());
                            e.printStackTrace();
                        }
                    });
                }
            } catch (IOException e) {
                player.sendMessage("§cFehler beim Laden des Schematics: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}