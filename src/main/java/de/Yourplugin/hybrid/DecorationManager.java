package de.yourplugin.hybrid;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Verwalter für Deko-Schematics (z.B. Lampen, Bäume, Bänke).
 * Nutzt WorldEdit, um sie an Positionen einzufügen.
 */
public class DecorationManager {

    private final File schematicDir;
    private final boolean lampsEnabled;
    private final boolean treesEnabled;
    private final boolean benchesEnabled;

    public DecorationManager(FileConfiguration cfg) {
        // Lies Einstellungen aus der config.yml
        this.lampsEnabled = cfg.getBoolean("decorations.street-lamps.enabled", false);
        this.treesEnabled = cfg.getBoolean("decorations.trees.enabled", false);
        this.benchesEnabled = cfg.getBoolean("decorations.benches.enabled", false);

        // Schematics liegen unter /plugins/Hybrid/schematics
        this.schematicDir = new File("plugins/Hybrid/schematics");
        if (!schematicDir.exists()) schematicDir.mkdirs();
    }

    /**
     * Fügt eine Schematic ein (Bytearray-Version).
     */
    public void pasteSchematic(World world, byte[] data, int x, int y, int z) {
        try (InputStream in = new ByteArrayInputStream(data)) {
            ClipboardFormat format = ClipboardFormats.findByInputStream(in);
            if (format == null) return;

            try (ClipboardReader reader = format.getReader(new ByteArrayInputStream(data))) {
                Clipboard clipboard = reader.read();
                ClipboardHolder holder = new ClipboardHolder(clipboard);

                try (EditSession editSession = WorldEdit.getInstance()
                        .newEditSession(BukkitAdapter.adapt(world))) {
                    Operation op = holder.createPaste(editSession)
                            .to(com.sk89q.worldedit.math.BlockVector3.at(x, y, z))
                            .ignoreAirBlocks(false)
                            .build();
                    Operations.complete(op);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Fügt eine Schematic von Datei ein.
     */
    public void pasteFromFile(World world, String fileName, Location loc) {
        File schematicFile = new File(schematicDir, fileName);
        if (!schematicFile.exists()) {
            System.out.println("Schematic fehlt: " + schematicFile.getAbsolutePath());
            return;
        }
        try (InputStream in = new FileInputStream(schematicFile)) {
            ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
            if (format == null) return;

            try (ClipboardReader reader = format.getReader(in)) {
                Clipboard clipboard = reader.read();
                ClipboardHolder holder = new ClipboardHolder(clipboard);

                try (EditSession editSession = WorldEdit.getInstance()
                        .newEditSession(BukkitAdapter.adapt(world))) {
                    Operation op = holder.createPaste(editSession)
                            .to(com.sk89q.worldedit.math.BlockVector3.at(
                                    loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()))
                            .ignoreAirBlocks(false)
                            .build();
                    Operations.complete(op);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Beispiel-Wrapper
    public void placeLamp(World world, Location loc) {
        if (!lampsEnabled) return;
        pasteFromFile(world, "lamp.schem", loc);
    }

    public void placeTree(World world, Location loc) {
        if (!treesEnabled) return;
        pasteFromFile(world, "tree.schem", loc);
    }

    public void placeBench(World world, Location loc) {
        if (!benchesEnabled) return;
        pasteFromFile(world, "bench.schem", loc);
    }
}