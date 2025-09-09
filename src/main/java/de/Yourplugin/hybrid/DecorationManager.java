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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DecorationManager {

    public static void pasteSchematic(Player player, byte[] schematicData, Location location) {
        try {
            InputStream in = new ByteArrayInputStream(schematicData);

            // NEU: findByInputStream statt detect()
            ClipboardFormat format = ClipboardFormats.findByInputStream(in);

            if (format == null) {
                player.sendMessage("§cUnbekanntes Schematic-Format!");
                return;
            }

            try (ClipboardReader reader = format.getReader(in)) {
                Clipboard clipboard = reader.read();
                World adaptedWorld = new BukkitWorld(location.getWorld());

                ClipboardHolder holder = new ClipboardHolder(clipboard);

                Bukkit.getScheduler().runTask(WorldgenPlugin.getInstance(), () -> {
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
    }
}