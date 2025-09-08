package de.Yourplugin.hybrid;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuyBuildingCommand implements CommandExecutor {

    private final HybridSurvivalPluginConfig plugin;
    private final BuildingManager buildingManager;

    public BuyBuildingCommand(HybridSurvivalPluginConfig plugin) {
        this.plugin = plugin;
        this.buildingManager = plugin.getBuildingManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Nur Spieler können Gebäude kaufen.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Benutzung: /buybuilding <Adresse>");
            return true;
        }

        String address = args[0];
        Location loc = player.getLocation();

        if (buildingManager.hasBuilding(address)) {
            BuildingData existing = buildingManager.getBuilding(address);
            player.sendMessage(ChatColor.RED + "Dieses Gebäude gehört bereits " + existing.getOwner() + ".");
            return true;
        }

        buildingManager.addBuilding(address, loc, player.getName(), 1000.0);
        player.sendMessage(ChatColor.GREEN + "Du hast das Gebäude bei '" + address + "' erfolgreich gekauft!");
        return true;
    }
}
