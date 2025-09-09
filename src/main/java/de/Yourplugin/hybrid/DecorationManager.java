package de.yourplugin.hybrid;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import org.bukkit.World;

import java.io.ByteArrayInputStream;

public class DecorationManager {

    // Konstruktor nimmt config oder plugin falls benötigt
    public DecorationManager(/* config param if needed */) { }

    public void pasteSchematic(World world, byte[] data, int x, int y, int z) {
        try {
            ClipboardFormat format = ClipboardFormats.findByFile(new ByteArrayInputStream(data));
            if (format == null) return;
            try (com.sk89q.worldedit.extent.clipboard.io.ClipboardReader reader = format.getReader(new ByteArrayInputStream(data))) {
                com.sk89q.worldedit.extent.clipboard.Clipboard clipboard = reader.read();
                com.sk89q.worldedit.session.ClipboardHolder holder = new com.sk89q.worldedit.session.ClipboardHolder(clipboard);
                try (EditSession editSession = WorldEdit.getInstance().newEditSession(com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(world))) {
                    Operation op = holder.createPaste(editSession)
                            .to(com.sk89q.worldedit.math.BlockVector3.at(x, y, z))
                            .ignoreAirBlocks(false)
                            .build();
                    Operations.complete(op); // statt .paste()
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Beispiel wrapper für later
    public void placeLamp(World world, org.bukkit.Location loc) {
        // implement using pasteSchematic or direct block placement
    }
}