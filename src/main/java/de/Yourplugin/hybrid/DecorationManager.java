package de.yourplugin.hybrid;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.World;

import java.io.ByteArrayInputStream;

public class DecorationManager {

    public DecorationManager() { }

    public void pasteSchematic(World world, byte[] data, int x, int y, int z) {
        try (ByteArrayInputStream in = new ByteArrayInputStream(data)) {
            ClipboardFormat format = ClipboardFormats.detect(in);
            if (format == null) return;

            try (ClipboardReader reader = format.getReader(new ByteArrayInputStream(data))) {
                Clipboard clipboard = reader.read();
                ClipboardHolder holder = new ClipboardHolder(clipboard);

                try (EditSession editSession = WorldEdit.getInstance()
                        .newEditSession(com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(world))) {
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

    // Beispiel wrapper für später
    public void placeLamp(World world, org.bukkit.Location loc) {
        // implement using pasteSchematic or direct block placement
    }
}