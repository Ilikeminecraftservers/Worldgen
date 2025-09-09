package de.yourplugin.hybrid;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Ersetzt Straßen- und Parkplatz-Blöcke durch die in der Config gesetzten Materialien.
 * -> Funktioniert ähnlich wie ein //replace: vorhandene Blöcke im Bereich werden überschrieben.
 */
public class StreetGenerator {

    private final Material streetBlock;
    private final Material lineBlock;
    private final Material parkingBlock;
    private final Material parkingLineBlock;
    private final int streetWidth;
    private final int lineWidth;

    public StreetGenerator(FileConfiguration cfg) {
        this.streetBlock = Material.matchMaterial(cfg.getString("streets.block", "BLUE_ICE"));
        this.lineBlock = Material.matchMaterial(cfg.getString("streets.line-block", "ICE"));
        this.parkingBlock = Material.matchMaterial(cfg.getString("parkings.block", "PACKED_ICE"));
        this.parkingLineBlock = Material.matchMaterial(cfg.getString("parkings.line-block", "ICE"));
        this.streetWidth = cfg.getInt("streets.width", 5);
        this.lineWidth = cfg.getInt("streets.line-width", 1);
    }

    /**
     * Erzeugt eine einfache "Straße" aus Blue Ice + Linien (ICE).
     */
    public void generateStreet(World world, Location origin) {
        int baseY = origin.getBlockY();
        int halfWidth = streetWidth / 2;

        for (int dx = -halfWidth; dx <= halfWidth; dx++) {
            for (int dz = -16; dz <= 16; dz++) {
                Location blockLoc = origin.clone().add(dx, 0, dz);
                if ((dx == 0 || Math.abs(dx) <= lineWidth) && lineBlock != null) {
                    // Mittellinie aus ICE
                    world.getBlockAt(blockLoc).setType(lineBlock, false);
                } else if (streetBlock != null) {
                    // Rest der Straße aus BLUE_ICE
                    world.getBlockAt(blockLoc).setType(streetBlock, false);
                }
            }
        }
    }

    /**
     * Erzeugt eine einfache Parkplatz-Fläche aus Packed Ice + Linien (ICE).
     */
    public void generateParking(World world, Location origin) {
        int baseY = origin.getBlockY();
        int width = 6;
        int length = 12;

        for (int dx = 0; dx < width; dx++) {
            for (int dz = 0; dz < length; dz++) {
                Location blockLoc = origin.clone().add(dx, 0, dz);
                if (dx % 3 == 0 && parkingLineBlock != null) {
                    // Linien zwischen Parkplätzen
                    world.getBlockAt(blockLoc).setType(parkingLineBlock, false);
                } else if (parkingBlock != null) {
                    // Parkplatzfläche
                    world.getBlockAt(blockLoc).setType(parkingBlock, false);
                }
            }
        }
    }
}