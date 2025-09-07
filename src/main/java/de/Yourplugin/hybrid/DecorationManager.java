package de.yourplugin.hybrid;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DecorationManager {

    private final boolean lampsEnabled;
    private final boolean treesEnabled;
    private final boolean benchesEnabled;
    private final String lampPath;
    private final String treePath;
    private final String benchPath;

    public DecorationManager(org.bukkit.configuration.file.FileConfiguration cfg) {
        lampsEnabled = cfg.getBoolean("decorations.street-lamps.enabled", false);
        treesEnabled = cfg.getBoolean("decorations.trees.enabled", false);
        benchesEnabled = cfg.getBoolean("decorations.benches.enabled", false);
        lampPath = cfg.getString("decorations.street-lamps.schematic-path", "schematics/lamps/");
        treePath = cfg.getString("decorations.trees.schematic-path", "schematics/trees/");
        benchPath = cfg.getString("decorations.benches.schematic-path", "schematics/benches/");
    }

    public void placeLamp(World world, Location loc) {
        if (!lampsEnabled) return;
        pasteSchematic(new File(lampPath + "lamp.schem"), world, loc);
    }

    public void placeTree(World world, Location loc) {
        if (!treesEnabled) return;
        pasteSchematic(new File(treePath + "tree.schem"), world, loc);
    }

    public void placeBench(World world, Location loc) {
        if (!benchesEnabled) return;
        pasteSchematic(new File(benchPath + "bench.schem"), world, loc);
    }

    private void pasteSchematic(File file, World world, Location loc) {
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        if (format == null) return;
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            Clipboard clipboard = reader.read();
            ClipboardHolder holder = new ClipboardHolder(clipboard);
            try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))) {
                holder.createPaste(editSession)
                        .to(BukkitAdapter.asBlockVector(loc))
                        .ignoreAirBlocks(false)
                        .build()
                        .paste();
                editSession.flushQueue();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}