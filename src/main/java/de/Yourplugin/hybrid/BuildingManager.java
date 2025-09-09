package de.yourplugin.hybrid;

import org.bukkit.Location;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BuildingManager {

    private final Map<String, BuildingData> buildings = new HashMap<>();

    public void addBuilding(String address, Location location, String owner, double price) {
        buildings.put(address, new BuildingData(location, owner, price));
    }

    public BuildingData getBuilding(String address) {
        return buildings.get(address);
    }

    public void removeBuilding(String address) {
        buildings.remove(address);
    }

    public boolean hasBuilding(String address) {
        return buildings.containsKey(address);
    }

    public Collection<BuildingData> getAllBuildings() {
        return Collections.unmodifiableCollection(buildings.values());
    }

    public Map<String, BuildingData> getAllBuildingsMap() {
        return Collections.unmodifiableMap(buildings);
    }
}