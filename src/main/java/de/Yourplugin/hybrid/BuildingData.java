package de.yourplugin.hybrid;

import org.bukkit.Location;
import java.util.UUID;

public class BuildingData {
    private final Location location;
    private String owner; // Spielername
    private final double price;
    private UUID ownerUUID; // optional

    public BuildingData(Location location, String owner, double price) {
        this.location = location;
        this.owner = owner;
        this.price = price;
    }

    public Location getLocation() {
        return location;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getPrice() {
        return price;
    }

    // Address helper: use world coords if you need a string
    public String getAddress() {
        return location.getWorld().getName() + ":" + location.getBlockX() + "," + location.getBlockZ();
    }

    public boolean isPurchased() {
        return owner != null && !owner.isEmpty();
    }
}