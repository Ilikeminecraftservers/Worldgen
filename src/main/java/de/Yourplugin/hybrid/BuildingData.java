package de.yourplugin.hybrid;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.UUID;

public class BuildingData {
    private Location location;
    private String street;
    private int number;
    private UUID owner;

    public BuildingData(Location loc, String street, int number){
        this.location = loc;
        this.street = street;
        this.number = number;
    }

    public String getAddress(){ return street+" "+number; }
    public Location getLocation(){ return location; }

    public void setOwner(UUID owner){ this.owner=owner; }
    public boolean isPurchased(){ return owner!=null; }
    public boolean isOwner(Player p){ return owner!=null && owner.equals(p.getUniqueId()); }
}
