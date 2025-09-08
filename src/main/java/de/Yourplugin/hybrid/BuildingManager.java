package de.Yourplugin.hybrid;

import org.bukkit.Location;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BuildingManager {

    // Key = Adresse (z. B. "Hauptstraße 12"), Value = BuildingData
    private final Map<String, BuildingData> buildings = new HashMap<>();

    /**
     * Gebäude hinzufügen
     */
    public void addBuilding(String address, Location location, String owner, double price) {
        buildings.put(address, new BuildingData(location, owner, price));
    }

    /**
     * Gebäude abrufen
     */
    public BuildingData getBuilding(String address) {
        return buildings.get(address);
    }

    /**
     * Alle Gebäude abrufen
     */
    public Collection<BuildingData> getAllBuildings() {
        return buildings.values();
    }

    /**
     * Gebäude entfernen
     */
    public void removeBuilding(String address) {
        buildings.remove(address);
    }

    /**
     * Prüfen ob ein Gebäude existiert
     */
    public boolean hasBuilding(String address) {
        return buildings.containsKey(address);
    }
}
